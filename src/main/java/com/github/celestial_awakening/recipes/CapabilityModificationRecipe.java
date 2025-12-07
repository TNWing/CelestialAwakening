package com.github.celestial_awakening.recipes;

public interface CapabilityModificationRecipe {
    class CapabilityResult{
        public String getCapabilityName() {
            return capabilityName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String[] getParam() {
            return param;
        }

        final String capabilityName;
        final String methodName;
        final String[] param;

        public CapabilityResult(String capabilityName, String methodName,String[] param) {
            this.capabilityName = capabilityName;
            this.methodName = methodName;
            this.param = param;
        }
    }

    class Requirement {
        public String getCapabilityName() {
            return capabilityName;
        }

        public String getMethodName() {
            return methodName;
        }

        public String getType() {
            return type;
        }

        public String getResult() {
            return result;
        }

        final String capabilityName;
        final String methodName;
        final String type;
        final String result;

        public Requirement(String capabilityName, String methodName, String type, String result) {
            this.capabilityName = capabilityName;
            this.methodName = methodName;
            this.type = type;
            this.result = result;
        }
    }
}
