# Fix Check Monitor Button Clickability When Waiting

## Tasks
- [x] Edit dashboard.php to remove 'waiting' from the disabled status array
- [x] Verify the button is now clickable when status is 'waiting'
- [x] Test the monitor link functionality

## Details
The issue is in Appweb/User/dashboard.php where the "Check Monitor" button is disabled when the ticket status is 'waiting'. By removing 'waiting' from the disabled statuses array, the button will remain enabled and allow users to check the monitor while waiting.

## Update
The change has been applied successfully. The $start_charging_disabled variable now only disables the button for 'charging' and 'completed' statuses, allowing it to be clickable when 'waiting'. The vehicle action button (Check Monitor) was already enabled for 'waiting' status, and the hero "Start Charging" button's JavaScript check may need adjustment if intended to be clickable during waiting.
