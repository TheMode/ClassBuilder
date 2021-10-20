# ClassBuilder

# Overview
Give you the ability to create Java code... using Java, the main goal is to generate code during run time, it can be used to create a scripting language.
### TODO list ###
- Flow control
- Inheritance
- Parameter operator
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
method.addStatement(Statement.createVariable(String.class, "varName", Parameter.literal("default value")));
...
classBuilder.addMethod(method);
```

### Statements ###
Statements are the core of the library, without statement your code won't do anything.
It includes field/variable manipulation and method execution

### Flow control ###
```java
BooleanExpression[] conditions = BooleanExpression.multi(BooleanExpression.not_null(Parameter.variable("varTest")));
Statement[] statements = Statement.multi(Statement.setVariable("varTest", Parameter.literal("bb")), Statement.callMethod(print, Parameter.literal("CONDITION TRUE")));

FlowControl flow = FlowControl.if_(conditions, statements)
                .else_if(BooleanExpression.greater(Parameter.literal(1), Parameter.literal(1)), Statement.callMethod(print, Parameter.literal("ELSE IF")))
                .else_(Statement.callMethod(print, Parameter.literal("CONDITION FALSE")));
method.addStatements(Statement.createFlowControl(flow));
```
