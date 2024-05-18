A compiler plugin that automatically generates getters and setters of enum class for annotated properties of `String` and `List<String>`.

# Sample
```kotlin
data class A(
    @EnumAccessor(accessorName = "mutableType", Type::class)
    var mutable: String,
    @EnumAccessor(accessorName = "readOnlyType", Type::class)
    val readOnly: String,
    @EnumAccessor(accessorName = "mutableTypes", Type::class)
    var mutableList: List<String>,
    @EnumAccessor(accessorName = "readOnlyTypes", Type::class)
    val readOnlyList: List<String>
)

@ParsableEnum(fallback = "Unknown")
enum class Type(val stringValue: String = "") {
    A("a"),
    B("b"),
    C("c"),
    Unknown;
}

```

```kotlin
val a = A(
    mutable = "a",
    readOnly = "b",
    mutableList = listOf("a", "b", "c"),
    readOnlyList = listOf("invalid")
)

// setter is automatically generated
a.mutableType = Type.C
a.mutableTypes = listOf(Type.A, Type.B)

a.readOnly // => Type.B
a.readOnlyList // => [Unknown] fallback to Unknown
```

# Limitation
KSP does not allow code modification, so it is not possible to generate getters and setters for private properties.