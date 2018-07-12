# Coding Standards
To keep our code base consistent, we require all contributors to adhere to a strict set of coding standards. By following these standards:
- Contributors can focus on the ideas behind the code without getting sidetracked by implementation details.
- Auto-formatting tools can be used without polluting version control with large diffs.
- Several classes of bugs can be eliminated straight away.

The standards have been divided into three categories:
- Kotlin.
- Gradle.
- Miscellaneous.

A file is said to be compliant if and only if it follows the applicable standards outlined in this document, and is consistent with the IntelliJ automatic formatting definition found [here](IntelliJ-Code-Auto-Formatting-Rules.xml).

## Kotlin
All Kotlin files comply with the official standards defined [here](https://android.github.io/kotlin-guides/), with two exceptions:
- Lines are wrapped at 120 characters instead of 100.
- Expression functions may be used for multiline functions if the entire function is a reactive stream creation such as:
```kotlin
fun callApi() = Completable.fromRunnable {
  apiProvider.doSomething()
  Logger.i("Called API successfully.")
}
```

Regarding identifier abbreviations:
- Only use abbreviations that aid readability. Never use abbreviations just to save typing.
- Never shorten words by removing non-contiguous letters from the word (e.g. adapter to adptr).
- Only use unambiguous abbreviations which you would expect every programmer to know (e.g. auth, num, XML, ID and CSS etc.).

Regarding units:
- When representing quantities using types that lack implicit unit support, always append the unit to the name of the variable/method.
- Use full units since camel case can interfere with the metric system. For example, does lengthMMetres represent a length in millimetres or megametres?
- Always write units as plurals.
- Always use the metric system unless the context justifies non-metric units. For example, inches are a valid unit when referring to screen size.

Here's an example of a method declaration with correct units:
```kotlin
fun getCurrentAmps(resistenceKiloOhms: Int, voltageVolts: Int): Int { /* ... */}
```

Never use Hungarian notation for anything, ever.

## Gradle
All gradle files:
- Use spaces for indentation.
- Are wrapped at 120 characters.
- Use UTF-8 encoding.

Indent new scopes with two spaces, and use four spaces for a continuation indent.

## Miscellaneous
Never declare or instantiate more than one variable in a single line.

Make liberal use of whitespace. Every member must be separated by one blank line, and blank lines should be used within methods to aid readability wherever possible.

Never use @author tags or any other source code marker that identifies ownership. Credit is established by Git, not the source.