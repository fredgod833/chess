package com.fgodard.security;

import java.security.Principal;

/**
 * <p> This class implements the <code>Principal</code> interface
 * and represents a Cylande user.
 *
 * <p> Principals such as this <code>AbstractPrincipal</code>
 * may be associated with a particular <code>Subject</code>
 * to augment that <code>Subject</code> with an additional
 * identity.  Refer to the <code>Subject</code> class for more information
 * on how to achieve this.  Authorization decisions can then be based upon 
 * the Principals associated with a <code>Subject</code>.
 * 
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
public abstract class AbstractPrincipal implements Principal
{
  private String name = null;

  /**
   * Create a AbstractPrincipal with a given username.
   * <p>
   * @param name the username for this user.
   * @exception NullPointerException if the <code>name</code>
   *			is <code>null</code>.
   */
  protected AbstractPrincipal(String name) {
    if (name == null)
      throw new NullPointerException("Principal name cannot be null");
    this.name = name;
  }

  /**
   * Return the name of this <code>AbstractPrincipal</code>.
   * <p>
   * @return the name of this <code>AbstractPrincipal</code>.
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * Return a hash code for this <code>AbstractPrincipal</code>.
   * <p>
   * @return a hash code for this <code>AbstractPrincipal</code>.
   */
  @Override
  public int hashCode()
  {
    return name.hashCode();
  }

  /**
   * Return a string representation of this <code>AbstractPrincipal</code>.
   * <p>
   * @return a string representation of this <code>AbstractPrincipal</code>.
   */
  @Override
  public abstract String toString();

  /**
   * Compares the specified Object with this <code>Principal</code> implementation
   * for equality. Returns true if the types ant names are the sames.
   * <p>
   * @param o Object to be compared for equality with this
   *		<code>Principal</code>.
   * @return true if the specified Object is equal to this
   *		<code>Principal</code>.
   */
  @Override
  public abstract boolean equals(Object o);

}
