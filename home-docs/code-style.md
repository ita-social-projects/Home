# Code style guidelines

Import [project code style file](.github/checkstyle-configyration.xml) into IntelliJ to get correct formatting and ability to
use "optimize imports" and "reformat code" actions. To do this you will need to
install [CheckStyle-IDEA plugin](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea). Go to File->Settings->
Editor->Code Style. Under "Scheme":"Show Scheme Actions" import Checkstyle Configuration from your local
repository (`.github/checkstyle-configyration.xml`).

Alternatively import (`.github/home-code-style.xml`) to your IDE as described above.

### Please, follow next formatting:

- Line length of `120` symbols.
- Indentation basic offset of `4`.
- Abbreviation length up to `4` symbols.
- Avoid inline annotations.
- Array declaration in Java style with trailing comma.
- Avoid star import and follow import order using empty line separator between groups:

    - static imports

    - standard java packages

    - special imports

    - third-party packages

- Operators on new line.
- Line wrapping - end of line:

    - comma, ellipsis, array declarator.

- Line wrapping - new line:

    - dot, method reference.