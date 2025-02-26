# Peliapuriapp

This is a Java Web Programming exercise related to University of Applied Sciences (SAVONIA). The main purpose is a frontend-focused exercise using the Java Vaadin Flow. The idea of ​​the app itself is to be a tool for speech therapists who use games in their teaching. The app could also be used for other similar purposes or just as some note-taking tool. All information/data is fictional for the demo, and the project is not suitable for official use as such.

## Running the application

Visual Studio Code

Open Folder in VS Code to open the project. VS Code automatically recognizes that this is a Maven-based Java project (requires Java Extension Pack). Project is now ready to run. Run Application.java.

IDE

Open the project in an IDE. You can download the [IntelliJ community edition](https://www.jetbrains.com/idea/download) if you do not have a suitable IDE already.
Once opened in the IDE, locate the `Application` class and run the main method using "Debug".

For more information on installing in various IDEs, see [how to import Vaadin projects to different IDEs](https://vaadin.com/docs/latest/getting-started/import).

If you install the Vaadin plugin for IntelliJ, you should instead launch the `Application` class using "Debug using HotswapAgent" to see updates in the Java code immediately reflected in the browser.

## Project structure

- `MainLayout.java` in `src/main/java` contains the navigation setup (i.e., the
  side/top bar and the main menu). This setup uses
  [App Layout](https://vaadin.com/docs/components/app-layout).
- `views` package in `src/main/java` contains the server-side Java views of your application.
- `views` folder in `src/main/frontend` contains the client-side JavaScript views of your application.
- `themes` folder in `src/main/frontend` contains the custom CSS styles.
