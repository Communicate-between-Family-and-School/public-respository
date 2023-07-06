package com.example.myapplication;

public class IDENTITY {
    public enum IdentityType {
        NO_IDENTITY("未设置身份"),
        STUDENT("学生"),
        TEACHER("教师"),
        PARENT("家长"),
        LEADER("领导");

        private String identity;

        IdentityType(String identity) {
            this.identity = identity;
        }

        public String getDisplayName() {
            return identity;
        }
    }

    private IdentityType currentIdentity;

    public IDENTITY() {
        currentIdentity = IdentityType.NO_IDENTITY;
    }

    public IDENTITY(IdentityType identityType) {
        currentIdentity = identityType;
    }

    public void setIdentity(IdentityType identityType) {
        currentIdentity = identityType;
    }

    public IdentityType getCurrentIdentity() {
        return currentIdentity;
    }
}
