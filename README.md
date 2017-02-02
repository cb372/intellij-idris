# intellij-idris

## How to hack on the plugin

1. Clone this repo

2. Download the latest IntelliJ Community Edition snapshot, so we can compile using sbt: `sbt updateIdea`

3. Open the project in IntelliJ, importing it as an SBT project.

4. OK, this is where it gets nasty... Open `.idea/modules/intellij-idris.iml` in an editor and make the following changes.
   * In the top-level `<module>` element, change the `type` to `PLUGIN_MODULE`
   * On the next line insert this `<component>` element: `<component name="DevKit.ModuleBuildProperties" url="file://$MODULE_DIR$/../../resources/META-INF/plugin.xml" />`

5. Open File > Project Structure.... For the module, change the Module SDK to be IntelliJ IDEA.

6. Go to Run > Edit Configurations... Add a Plugin run configuration.

After all that, you should be able to use the Run and Debug buttons to start an instance of IntelliJ with the plugin installed.

## Implementation notes

See NOTES.md
