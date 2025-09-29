# Delete Account Feature Implementation

## Overview
Implementing a confirmation modal for account deletion in profile.php, with a 3-second timer on the delete button, and backend logic to remove the user row from the 'users' table.

## Steps

- [ ] Step 1: Create new file Appweb/User/delete_account.php with session validation, POST handling for deletion (DELETE from users WHERE username = :username, unlink profile picture if exists, session_destroy, redirect to index.php).

- [ ] Step 2: Edit Appweb/User/profile.php to add HTML for delete confirmation modal (structure similar to existing photo modal: overlay, content with warning message, Cancel and Delete buttons at bottom).

- [ ] Step 3: Edit Appweb/User/profile.php to add onclick="openDeleteModal()" to the existing delete button in form-actions.

- [ ] Step 4: Edit Appweb/User/profile.php to add JavaScript functions in the <script> section: openDeleteModal() (show modal, start 3s countdown on delete button, disable until 0), closeDeleteModal(), handle delete click (AJAX POST to delete_account.php with username, on success: session clear via redirect, show success message).

- [ ] Step 5: Test the feature (login, navigate to profile, click delete, verify modal/timer, confirm deletion in DB, check redirect).

- [ ] Step 6: Mark all steps complete and remove this TODO if successful.

