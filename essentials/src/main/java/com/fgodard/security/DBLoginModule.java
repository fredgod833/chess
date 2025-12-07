package com.fgodard.security;

import java.io.PrintWriter;
import java.io.StringWriter;

import java.security.Principal;

import java.sql.Connection;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;

import javax.naming.InitialContext;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import javax.sql.DataSource;

/**
 * This class is a LoginModule used for JAAS authentication
 * User is loaded from UTILISATEURS table, from Cylande database
 */
public class DBLoginModule implements LoginModule
{
  protected Principal[] _authPrincipals;
  protected CallbackHandler _callbackHandler;
  protected boolean _commitSucceeded;
  // configuration options
  protected boolean _debug;
  // username and password
  protected String _name;
  protected Map _options;
  protected char[] _password;
  protected Map _sharedState;
  // initial state
  protected Subject _subject;
  // the authentication status
  protected boolean _succeeded;
  private static final String _SQLRequest = "SELECT U.NOMUTILISATEUR, U.MOTDEPASSE PASS FROM UTILISATEURS U WHERE NomUtilisateur=?";
  //private static final Logger _log = Logger.getLogger(DBLoginModule.class);
  //private static final Logger _logHisto = Logger.getLogger("LoginTracking");

  /**
   * Initialize this <code>DBLoginModule</code>.
   * <p>
   * @param subject         the <code>Subject</code> to be authenticated. <p>
   * @param callbackHandler a <code>CallbackHandler</code> for communicating
   *                        with the end user (prompting for usernames and
   *                        passwords, for example). <p>
   * @param sharedState     shared <code>LoginModule</code> state. <p>
   * @param options         options specified in the login
   *                        <code>Configuration</code> for this particular
   *                        <code>LoginModule</code>.
   */
  public void initialize(Subject subject, CallbackHandler callbackHandler, Map sharedState, Map options)
  {
    this._subject = subject;
    this._callbackHandler = callbackHandler;
    this._sharedState = sharedState;
    this._options = options;
    // initialize any configured options
    _debug = "true".equalsIgnoreCase((String)_options.get("debug"));
    if (debug())
    {
      printConfiguration(this);
    }
  }

  public final boolean debug()
  {
    return _debug;
  }

  protected Principal[] getAuthPrincipals()
  {
    return _authPrincipals;
  }

  /**
   * Authenticate the user by prompting for a username and password.
   * <p>
   * @return true if the authentication succeeded, or false if this
   *         <code>LoginModule</code> should be ignored.
   * @throws LoginException if this <code>LoginModule</code>
   *                        is unable to perform the authentication.
   */
  public boolean login() throws LoginException
  {
    if (debug())
    {
      System.out.println("\t\t[DBLoginModule] login");
    }
    if (_callbackHandler == null)
    {
      String message = "Error: no CallbackHandler available " + "to garner authentication information from the user";
      throw new LoginException(message);
    }
    // Setup default callback handlers.
    Callback[] callbacks = new Callback[]
      { new NameCallback("Username: "), new PasswordCallback("Password: ", false) };
    try
    {
      _callbackHandler.handle(callbacks);
    }
    catch (Exception e)
    {
      _succeeded = false;
      //StringWriter sw = new StringWriter();
      //PrintWriter pw = new PrintWriter(sw);
      //e.printStackTrace(pw);
      throw new LoginException(e.getMessage());
    }
    String username = ((NameCallback)callbacks[0]).getName();
    String password = new String(((PasswordCallback)callbacks[1]).getPassword());
    if (debug())
    {
      System.out.println("\t\t[DBLoginModule] username : " + username);
    }
    // Authenticate the user. On successfull authentication add principals
    // to the Subject. The name of the principal is used for authorization by
    // OC4J by mapping it to the value of the name attribute of the group
    // element in the security-role-mapping for the application.
    try
    {
      if (checkUserInDatabase(username, password))
      {
        _succeeded = true;
        _name = username;
        _password = password.toCharArray();
        _authPrincipals = new Principal[2];
        // Adding username as principal to the subject
        // Adding role developers to the subject
      }
    }
    catch (Exception e)
    {
      throw new LoginException("Error checking user/password");
    }
    ((PasswordCallback)callbacks[1]).clearPassword();
    callbacks[0] = null;
    callbacks[1] = null;
    if (debug())
    {
      System.out.println("\t\t[DBLoginModule] success : " + _succeeded);
    }
    if (!_succeeded)
    {
      throw new LoginException("Authentication failed");
    }
    return true;
  }

  /**
   * This method is called if the LoginContext's overall authentication succeeded
   * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules succeeded).
   * <p>
   * If this LoginModule's own authentication attempt succeeded
   * (checked by retrieving the private state saved by the <code>login</code> method),
   * then this method associates a <code>Principal</code> with the <code>Subject</code>
   * located in the <code>LoginModule</code>.  If this LoginModule's own authentication
   * attempted failed, then this method removes any state that was originally saved.
   * <p>
   * @return true if this LoginModule's own login and commit
   *         attempts succeeded, or false otherwise.
   * @throws LoginException if the commit fails.
   */
  public boolean commit() throws LoginException
  {
    try
    {
      if (_succeeded == false)
      {
        return false;
      }
      if (_subject.isReadOnly())
      {
        throw new LoginException("Subject is ReadOnly");
      }
      // add authenticated principals to the Subject
      if (getAuthPrincipals() != null)
      {
        for (int i = 0; i < getAuthPrincipals().length; i++)
        {
          if (!_subject.getPrincipals().contains(getAuthPrincipals()[i]))
          {
            _subject.getPrincipals().add(getAuthPrincipals()[i]);
          }
        }
      }
      // in any case, clean out state
      cleanup();
      if (debug())
      {
        printSubject(_subject);
      }
      _commitSucceeded = true;
      return true;
    }
    catch (Throwable t)
    {
      if (debug())
      {
        System.out.println(t.getMessage());
        t.printStackTrace();
      }
      //StringWriter sw = new StringWriter();
      //PrintWriter pw = new PrintWriter(sw);
      //t.printStackTrace(pw);
      throw new LoginException(t.toString());
    }
  }

  /**
   * This method is called if the LoginContext's overall authentication failed.
   * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules did not succeed).
   * <p>
   * If this LoginModule's own authentication attempt succeeded
   * (checked by retrieving the private state saved by the <code>login</code> and <code>commit</code> methods),
   * then this method cleans up any state that was originally saved.
   * <p>
   * @return false if this LoginModule's own login and/or commit attempts failed, and true otherwise.
   * @throws LoginException if the abort fails.
   */
  public boolean abort() throws LoginException
  {
    if (debug())
    {
      System.out.println("\t\t[SampleLoginModule] aborted authentication attempt.");
    }
    if (_succeeded == false)
    {
      cleanup();
      return false;
    }
    else if ((_succeeded == true) && (_commitSucceeded == false))
    {
      // login succeeded but overall authentication failed
      _succeeded = false;
      cleanup();
    }
    else
    {
      // overall authentication succeeded and commit succeeded,
      // but someone else's commit failed
      logout();
    }
    return true;
  }

  protected void cleanup()
  {
    _name = null;
    if (_password != null)
    {
      for (int i = 0; i < _password.length; i++)
      {
        _password[i] = ' ';
      }
      _password = null;
    }
  }

  protected void cleanupAll()
  {
    cleanup();
    if (getAuthPrincipals() != null)
    {
      for (int i = 0; i < getAuthPrincipals().length; i++)
      {
        _subject.getPrincipals().remove(getAuthPrincipals()[i]);
      }
    }
  }

  /**
   * Logout the user.
   * <p>
   * This method removes the <code>Principal</code>
   * that was added by the <code>commit</code> method.
   * <p>
   * @return true in all cases since this <code>LoginModule</code> should not be ignored.
   * @throws LoginException if the logout fails.
   */
  public boolean logout() throws LoginException
  {
    _succeeded = false;
    _commitSucceeded = false;
    cleanupAll();
    return true;
  }
  // helper methods //

  protected static void printConfiguration(DBLoginModule slm)
  {
    if (slm == null)
    {
      return;
    }
    System.out.println("\t\t[SampleLoginModule] configuration options:");
    if (slm.debug())
    {
      System.out.println("\t\t\tdebug = " + slm.debug());
    }
  }

  protected static void printSet(Set s)
  {
    try
    {
      Iterator principalIterator = s.iterator();
      while (principalIterator.hasNext())
      {
        Principal p = (Principal)principalIterator.next();
        System.out.println("\t\t\t" + p.toString());
      }
    }
    catch (Throwable t)
    {
    }
  }

  protected static void printSubject(Subject subject)
  {
    try
    {
      if (subject == null)
      {
        return;
      }
      Set s = subject.getPrincipals();
      if ((s != null) && (s.size() != 0))
      {
        System.out.println("\t\t[SampleLoginModule] added the following Principals:");
        printSet(s);
      }
      s = subject.getPublicCredentials();
      if ((s != null) && (s.size() != 0))
      {
        System.out.println("\t\t[SampleLoginModule] added the following Public Credentials:");
        printSet(s);
      }
    }
    catch (Throwable t)
    {
    }
  }

  /**
   * Use JDBC to load user from the database
   * <br>
   * "jdbc/CylandeDS" DataSource must be defined to establish the connection
   * 
   * 
   * @param username
   * @param password
   * @return
   */
  private boolean checkUserInDatabase(String username, String password) throws Exception
  {
    boolean success = false;
    ResultSet resultSet = null;
    PreparedStatement pstmt = null;
    Connection conn = null;
    try
    {
      Context initialContext = new InitialContext();
      DataSource ds = (DataSource)initialContext.lookup("java:comp/env/jdbc/UserDS");
      conn = ds.getConnection();
      pstmt = conn.prepareStatement(_SQLRequest, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      pstmt.setString(1, username);
      resultSet = pstmt.executeQuery();
      if (resultSet.next())
      {
        if (checkPass(password, resultSet.getString("PASS")))
        {
          success = true;
        }
      }
    }
    catch (Exception e)
    {
      if (debug())
      {
        System.out.println(e.getMessage());
        e.printStackTrace();
      }

      throw e;

    } finally {

      try
      {
        if (resultSet != null)
          resultSet.close();
      } catch (Exception e) {
        if (debug()) {
          e.printStackTrace(System.out);
        }
      }

      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        if (debug())
        {
          e.printStackTrace(System.out);
        }
      }

      try {
        if (conn != null)
          conn.close();
      } catch (Exception e) {
        if (debug())
        {
          e.printStackTrace(System.out);
        }
      }
    }
    return success;
  }

  private boolean checkPass(String typedPass, String storedPass)
  {
    boolean result = true;
    if (storedPass.length() != typedPass.length())
      return false;
    for (int i = 0; i < typedPass.length(); i++)
    {
      result &= (storedPass.charAt(i) == (char)(typedPass.charAt(i) + (i + 1) % 27));
    }
    return result;
  }
}
