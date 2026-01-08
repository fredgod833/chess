package com.fgodard.security;

/**
 * <p> This class extends AbstractPrincipal and therefore implements 
 * the <code>Principal</code> interface and represents a role.
 *
 * <p> Principals such as this <code>RolePrincipal</code>
 * may be associated with a particular <code>Subject</code>
 * to augment that <code>Subject</code> with an additional
 * identity.  Refer to the <code>Subject</code> class for more information
 * on how to achieve this.  Authorization decisions can then be based upon 
 * the Principals associated with a <code>Subject</code>.
 * 
 * @see java.security.Principal
 * @see javax.security.auth.Subject
 */
public class RolePrincipal extends AbstractPrincipal
{
  /**
   * Create a RolePrincipal with a given name.
   * <p>
   * @param name the name for this role.
   * @exception NullPointerException if the <code>name</code>
   *			is <code>null</code>.
   */
  public RolePrincipal(String name)
  {
  	super(name);
  }

  /**
   * Return a string representation of this <code>RolePrincipal</code>.
   * <p>
   * @return a string representation of this <code>RolePrincipal</code>.
   */
  public String toString()
  {
    return "[RolePrincipal] : " + getName();
  }

  /**
   * Compares the specified Object with this <code>RolePrincipal</code>
   * for equality.  Returns true if the given object is also a
   * <code>RolePrincipal</code> and the two RolePrincipals
   * have the same name.
   * <p>
   * @param o Object to be compared for equality with this
   *		<code>RolePrincipal</code>.
   * @return true if the specified Object is equal to this
   *		<code>RolePrincipal</code>.
   */
  @Override
  public boolean equals(Object o)
  {
    if (o == null)
      return false;
    if (this == o)
      return true;
    if (!(o instanceof RolePrincipal))
      return false;
    RolePrincipal that = (RolePrincipal)o;
    if (this.getName().equals(that.getName()))
      return true;
    return false;
  }

    @Override
    public int hashCode() {
        return this.getName().hashCode();
    }
  
}
