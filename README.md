# Incompatible Products Management (IPM)

<P align="middle">
  <img src="./lib/src/main/resources/IPM/photos/logo.png" width="200"/>
</P>

## Overview

This program was developed as part of an academic thesis on "Incompatible Products Management" presented by DJEZZAR Moncef, SADOUDI Abdessamad, and AMRI Taki. The program is designed to help manage hazardous materials and ensure the safety of personnel in various industries.

## Features

- Graphical visualization of incompatible products
- User-friendly interface built with JavaFX and MaterialFX

## Prerequisites

- Java JDK 20 or higher
- Gradle 7.4.2 or higher
- WiX Toolset v3.11.2 (for creating Windows installer)

## Dependencies

- JavaFX
- MaterialFX 11.16.1
- Apache POI 5.2.3
- SmartGraph 0.9.4
- JUnit Jupiter 5.8.1

## Installation

1. Clone the repository:
```bash
git clone https://github.com/MoncefDj/IPM.git
```

2. Navigate to the lib directory:
```bash
cd IPM/lib
```

3. Build the project:
```bash
gradle build
```

## Running the Application

To run the application, make sure you're in the lib directory and execute:
```bash
gradle run
```

## Creating Windows Installer

To create a Windows installer, make sure you're in the lib directory and execute:
```bash
gradle clean
gradle jpackage
```

The installer will be created in `build/jpackage/` directory.

## Contributing

Contributions to this project are welcome! If you find a bug or have a feature request, please open an issue or submit a pull request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Authors

- DJEZZAR Moncef
- SADOUDI Abdessamad
- AMRI Taki

---

Thank you for your interest in our program! We hope that it will be useful to you in managing incompatible products and ensuring workplace safety.

