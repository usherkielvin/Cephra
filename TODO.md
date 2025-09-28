# Task: Copy battery_level logic from dashboard.php to link.php

## Breakdown of Steps

1. **[x] Update link.php PHP logic:**
   - Add database fetch for battery_level similar to dashboard.php.
   - Include vehicle models, car_images, and vehicle_specs arrays from dashboard.php.
   - Adjust car_index generation in form submission to rand(0,8) to match specs.
   - Calculate current range and time_to_full based on battery_level, similar to dashboard.php.

2. **[x] Update the "With Car" display section in link.php:**
   - Replace hardcoded "Porsche" with dynamic model from $models[$car_index].
   - Update "Kms Remaining" to show calculated range.
   - Update "Time to Charge" to show calculated time_to_full.
   - Update Battery Level progress bar and text to use $db_battery_level or specs value.
   - Add Performance if possible (e.g., from specs or hardcoded).

3. **[x] Test and verify:**
   - Ensure no errors in PHP.
   - Check display updates correctly when car is linked.
   - Update TODO.md after each step.

Task completed successfully.
