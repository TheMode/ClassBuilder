# ClassBuilder

# Overview
Give you the ability to create java code... using java, the main goal is to generate code during run time, it can be used to create a scripting language.
### TODO list ###
- Flow control
- Inheritance
- Interface
- More statements
- More Utils class

# Usage
### Generating the class ###
```java
ClassBuilder classBuilder = ClassBuilder.createClass(ClassVersion.V1_8, "com.package.example.SampleClass");
...
Class result = classBuilder.load();
```

### Add field ###
```java
FieldBuilder field = FieldBuilder.createField(String.class, "fieldName", "default value");
field.setModifiers(Modifier.PUBLIC, Modifier.FINAL);
classBuilder.addField(field);
```

### Add method ###
```java
// void is the return type, String and int are arguments
MethodBuilder method = MethodBuilder.createMethod("main", void.class, String.class, int.class);
method.setModifiers(Modifier.PUBLIC, Modifier.STATIC);
method.addStatement(Statement.createVariable(String.class, "varName", Parameter.constant("default value")));
...
classBuilder.addMethod(method);
```

### Statements ###
Statements are the core of the library, without statement your code won't do anything.
It includes field/variable manipulation and method execution

### Flow control ###
In progress
