package cn.czcps.oa.entity;


public class Role {
    private long roleId;
    private String roleDescription;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleDescription;
    }

    public void setRoleName(String roleName) {
        this.roleDescription = roleName;
    }
}
