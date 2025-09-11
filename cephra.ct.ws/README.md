# CephraDomain - Domain Deployment Folder

This folder contains a copy of the mobileweb files specifically configured for domain deployment.

## Purpose
- **Main development**: Continue using `localhost` in the main Cephra folder
- **Domain deployment**: Copy files from this `CephraDomain` folder to your domain's file manager
- **No environment switching needed**: This folder is pre-configured for production

## Configuration
- **Database**: Always uses production database settings
- **Environment**: Set to `domain_production` 
- **No localhost detection**: Bypasses environment detection for consistent domain deployment

## Usage
1. Develop on localhost using the main `mobileweb` folder
2. When ready to deploy, copy files from `CephraDomain/mobileweb/` to your domain
3. No configuration changes needed - it's ready for production

## Files Included
- Complete mobileweb application
- Production database configuration
- All assets, CSS, JS, and PHP files
- Ready for direct domain deployment
