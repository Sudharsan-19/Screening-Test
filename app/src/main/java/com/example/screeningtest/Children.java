package com.example.screeningtest;

public class Children {
    private String ChildName;
    private String ChildAge;
    private String ChildGender;
    private String Childdob;
    private String ChildClass;
    private String TestResult1;
    private String TestResult2;

    public Children() {
    }

    public String getChildGender() {
        return ChildGender;
    }

    public void setChildGender(String childGender) {
        ChildGender = childGender;
    }

    public Children(String childName, String childAge, String childGender, String childdob, String childClass, String testResult1, String testResult2) {
        ChildName = childName;
        ChildAge = childAge;
        ChildGender = childGender;
        Childdob = childdob;
        ChildClass = childClass;
        TestResult1 = testResult1;
        TestResult2 = testResult2;
    }

    public String getChildName() {
        return ChildName;
    }

    public void setChildName(String childName) {
        ChildName = childName;
    }

    public String getChildAge() {
        return ChildAge;
    }

    public void setChildAge(String childAge) {
        ChildAge = childAge;
    }

    public String getChilddob() {
        return Childdob;
    }

    public void setChilddob(String childdob) {
        Childdob = childdob;
    }

    public String getChildClass() {
        return ChildClass;
    }

    public void setChildClass(String childClass) {
        ChildClass = childClass;
    }

    public String getTestResult1() {
        return TestResult1;
    }

    public void setTestResult1(String testResult1) {
        TestResult1 = testResult1;
    }

    public String getTestResult2() {
        return TestResult2;
    }

    public void setTestResult2(String testResult2) {
        TestResult2 = testResult2;
    }

    public Children(String childName, String childAge, String childdob, String childClass, String testResult1, String testResult2) {
        ChildName = childName;
        ChildAge = childAge;
        Childdob = childdob;
        ChildClass = childClass;
        TestResult1 = testResult1;
        TestResult2 = testResult2;
    }
}
