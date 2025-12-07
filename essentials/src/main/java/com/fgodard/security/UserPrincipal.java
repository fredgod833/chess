package com.cylande.framework.security;

import fr.fgodard.security.AbstractPrincipal;

/**
 * <p> This class extends AbstractPrincipal and therefore implements 
 * the <code>Principal</code> interface and represents a user.
 *
 * <p> Principals such as this <code>UserPrincipal</code>
 * may be associated with a particular <code>Subject</code>
 * to augment that <code>Subject</code> with an additional
 * identity.  Refer to the <code>Subject</code> class for more information
 * on how to achieve this.  Authorization decisions can then be based upon 
 * the Principals associated with a <code>Subject</code>.
 * 
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
public class UserPrincipal extends AbstractPrincipal
{
  /**
   * Create a UserPrincipal with a given username.
   * <p>
   * @param name the username for this user.
   * @exception NullPointerException if the <code>name</code>
   *			is <code>null</code>.
   */
  public UserPrincipal(String name)
  {
  	super(name);
  }

  /**
   * Return a string representation of this <code>UserPrincipal</code>.
   * <p>
   * @return a string representation of this <code>UserPrincipal</code>.
   */
  public String toString()
  {
    return "[UserPrincipal] : " + getName();
  }

  /**
   * Compares the specified Object with this <code>UserPrincipal</code>
   * for equality.  Returns true if the given object is also a
   * <code>UserPrincipal</code> and the two UserPrincipals
   * have the same username.
   * <p>
   * @param o Object to be compared for equality with this
   *		<code>UserPrincipal</code>.
   * @return true if the specified Object is equal to this
   *		<code>UserPrincipal</code>.
   */
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (this == o)
      return true;
    if (!(o instanceof UserPrincipal))
      return false;
    UserPrincipal that = (UserPrincipal)o;
    if (this.getName().equals(that.getName()))
      return true;
    return false;
  }
}
