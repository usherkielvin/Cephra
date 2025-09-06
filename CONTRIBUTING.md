# Contributing to Cephra

Thank you for your interest in contributing to Cephra! This document provides guidelines and information for contributors.

## 🎯 How Can I Contribute?

### 🐛 Reporting Bugs
- Use GitHub Issues to report bugs
- Include detailed steps to reproduce
- Provide system information (OS, Java version, etc.)
- Attach relevant log files or screenshots

### 💡 Suggesting Features
- Open a GitHub Issue with the "enhancement" label
- Describe the feature and its benefits
- Consider implementation complexity
- Check existing issues to avoid duplicates

### 💻 Code Contributions
- Fork the repository
- Create a feature branch
- Make your changes
- Test thoroughly
- Submit a pull request

## 🚀 Getting Started

### Prerequisites
- Java 21 or higher
- Maven 3.11.0 or higher
- MySQL 8.0+ or XAMPP
- Git
- IDE (NetBeans, IntelliJ, or VS Code)

### Development Setup

1. **Fork and Clone**
   ```bash
   git clone https://github.com/yourusername/cephra.git
   cd cephra
   ```

2. **Set Up Database**
   ```bash
   scripts/init-database.bat
   ```

3. **Build Project**
   ```bash
   mvn clean compile
   ```

4. **Run Tests**
   ```bash
   mvn test
   ```

## 📋 Development Guidelines

### Code Style

#### Java Code
- Follow Java naming conventions
- Use meaningful variable and method names
- Add comments for complex logic
- Keep methods focused and small
- Use proper indentation (4 spaces)

#### PHP Code
- Follow PSR-12 coding standards
- Use meaningful variable names
- Add PHPDoc comments
- Validate all inputs
- Use prepared statements for database queries

#### Database
- Use descriptive table and column names
- Add proper indexes
- Include foreign key constraints
- Document schema changes

### Commit Messages
Use clear, descriptive commit messages:
```
feat: add user registration validation
fix: resolve database connection timeout
docs: update installation guide
style: format Java code according to standards
refactor: simplify queue management logic
test: add unit tests for payment processing
```

### Pull Request Process

1. **Create Feature Branch**
   ```bash
   git checkout -b feature/your-feature-name
   ```

2. **Make Changes**
   - Write clean, well-documented code
   - Add tests for new functionality
   - Update documentation if needed

3. **Test Your Changes**
   ```bash
   mvn clean test
   scripts/run.bat  # Test the application
   ```

4. **Submit Pull Request**
   - Provide clear description of changes
   - Reference related issues
   - Include screenshots for UI changes
   - Ensure all tests pass

## 🏗️ Project Structure

### Java Components
- `src/main/java/cephra/Admin/` - Admin panel components
- `src/main/java/cephra/Phone/` - Customer mobile interface
- `src/main/java/cephra/Frame/` - Main application frames
- `src/main/java/cephra/db/` - Database connection classes
- `src/main/java/cephra/CephraDB.java` - Core database operations

### Web Components
- `mobileweb/` - Mobile web interface
- `api/` - PHP API endpoints
- `config/` - Configuration files

### Documentation
- `docs/` - Project documentation
- `scripts/` - Setup and utility scripts

## 🧪 Testing

### Java Testing
- Write unit tests for new methods
- Test database operations
- Verify UI components work correctly
- Test error handling scenarios

### Web Testing
- Test API endpoints
- Verify mobile responsiveness
- Check cross-browser compatibility
- Test user authentication flows

### Database Testing
- Test schema changes
- Verify data integrity
- Test performance with large datasets
- Check backup and restore procedures

## 📚 Documentation

### Code Documentation
- Add JavaDoc comments for public methods
- Include PHPDoc comments for PHP functions
- Document complex algorithms
- Explain business logic

### User Documentation
- Update README.md for new features
- Add setup instructions for new requirements
- Document configuration options
- Provide troubleshooting guides

## 🐛 Bug Reports

When reporting bugs, include:

1. **Environment Information**
   - Operating System
   - Java version
   - MySQL version
   - Browser (for web issues)

2. **Steps to Reproduce**
   - Detailed step-by-step instructions
   - Expected vs actual behavior
   - Screenshots or error messages

3. **Additional Context**
   - Frequency of occurrence
   - Workarounds (if any)
   - Related issues

## 💡 Feature Requests

When suggesting features:

1. **Problem Description**
   - What problem does this solve?
   - Who would benefit from this feature?

2. **Proposed Solution**
   - How should this feature work?
   - Any implementation ideas?

3. **Alternatives Considered**
   - Other ways to solve the problem
   - Why this approach is preferred

## 🔒 Security

- Never commit sensitive information (passwords, API keys)
- Use environment variables for configuration
- Validate all user inputs
- Follow secure coding practices
- Report security vulnerabilities privately

## 💬 Communication

### GitHub Issues
- Use for bug reports and feature requests
- Be respectful and constructive
- Provide clear, detailed information
- Respond to questions promptly

### Pull Requests
- Keep PRs focused and small
- Provide clear descriptions
- Respond to review feedback
- Update documentation as needed

## 🏆 Recognition

Contributors will be:
- Listed in the project README
- Credited in release notes
- Invited to join the development team (for significant contributions)

## 📋 Development Roadmap

### Current Priorities
- Enhanced payment processing
- Mobile app optimization
- Real-time notifications
- Multi-station support

### Future Features
- Advanced reporting
- API integration
- Cloud deployment
- Scalability improvements

## 🤝 Code of Conduct

### Our Pledge
We are committed to providing a welcoming and inclusive environment for all contributors.

### Expected Behavior
- Be respectful and inclusive
- Accept constructive criticism
- Focus on what's best for the community
- Show empathy towards others

### Unacceptable Behavior
- Harassment or discrimination
- Trolling or inflammatory comments
- Personal attacks
- Inappropriate language or imagery

## 📞 Contact

- **Project Lead**: Usher Kielvin Ponce
- **Email**: ponceud@students.nu-moa.edu.ph
- **GitHub**: [usherkielvin](https://github.com/usherkielvin)
- **LinkedIn**: [usherkielvinponce](https://linkedin.com/in/usherkielvinponce)

## 📄 License

By contributing to Cephra, you agree that your contributions will be licensed under the MIT License.

---

Thank you for contributing to Cephra! Your efforts help make EV charging more accessible and efficient. 🚀
