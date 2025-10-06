# Fix Check Monitor Button Clickability When Waiting

## Tasks
- [ ] Edit dashboard.php to remove 'waiting' from the disabled status array
- [ ] Verify the button is now clickable when status is 'waiting'
- [ ] Test the monitor link functionality

## Details
The issue is in Appweb/User/dashboard.php where the "Check Monitor" button is disabled when the ticket status is 'waiting'. By removing 'waiting' from the disabled statuses array, the button will remain enabled and allow users to check the monitor while waiting.
