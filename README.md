# ClassBuilder

# Overview
Give you the ability to create java code... using java.
The main goal is to generate code at run time, it can be used to create a scripting language.
# Usage
### Generating the class ###
```java
ClassBuilder classBuilder = ClassBuilder.createClass(ClassVersion.V1_8, "com.package.example.SampleClass");
...
Class result = classBuilder.load();
```

### Add field ###
```java
FieldBuilder field = createField(String.class, "fieldName", "default value");
field.setModifiers(Modifier.PUBLIC, Modifier.FINAL);
classBuilder.addField(field);
```

### Add method ###
```java
// void is the return type, String and int are arguments
MethodBuilder method = createMethod("main", void.class, String.class, int.class);
method.setModifiers(Modifier.PUBLIC, Modifier.STATIC);
method.addStatement(Statement.createVariable(String.class, "varName", "default value"));
...
classBuilder.addMethod(method);
```
