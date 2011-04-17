package org.ssh.pm.utils;

public enum EContentStatus {
    edit {
        @Override
        public String toString() {
            return "编辑";
        }
    },

    audit {
        @Override
        public String toString() {
            return "";
        }
    },

    publish {
        @Override
        public String toString() {
            return "发布";
        }
    },

    lock {
        @Override
        public String toString() {
            return "锁定";
        }
    },

    delete {
        @Override
        public String toString() {
            return "删除";
        }
    }
}
