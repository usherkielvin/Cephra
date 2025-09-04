w# Development Tools & Configuration

This folder contains development environment configurations and tools for the Cephra project.

## 📁 Contents

### `.vscode/`
VS Code workspace settings and configuration:
- **`settings.json`** - VS Code workspace settings including:
  - Java configuration (JavaSE-24)
  - File exclusions for build artifacts
  - Debug settings
  - Build configuration

## 🛠️ Development Setup

### VS Code Setup
1. Install the **Extension Pack for Java** in VS Code
2. The workspace settings in `.vscode/settings.json` will automatically configure:
   - Java runtime (JavaSE-24)
   - File exclusions for compiled classes and build directories
   - Debug settings for Java development

### Recommended VS Code Extensions
- **Extension Pack for Java** (Microsoft)
- **Maven for Java** (Microsoft)
- **Spring Boot Tools** (VMware)
- **GitLens** (GitKraken)
- **PHP Intelephense** (for web interface development)

## 🔧 Configuration Details

### Java Configuration
- **Runtime**: JavaSE-24
- **Build System**: Maven
- **Main Class**: `cephra.Launcher`

### File Exclusions
The following files/folders are excluded from VS Code file explorer:
- `*.class` - Compiled Java classes
- `target/` - Maven build directory
- `bin/` - Eclipse build directory
- `.classpath`, `.project` - Eclipse project files
- `.settings/` - Eclipse settings

## 📝 Notes

- These settings are workspace-specific and won't affect other projects
- The `.vscode` folder is excluded from Git (see `.gitignore`)
- Each developer can customize their own VS Code settings
- Settings are automatically applied when opening the project in VS Code

## 🚀 Getting Started

1. Open the project in VS Code
2. Install recommended extensions when prompted
3. The Java extension will automatically detect the Maven project
4. Use `Ctrl+Shift+P` → "Java: Reload Projects" if needed
5. Run the application using `scripts/run.bat` or through VS Code's run configuration
