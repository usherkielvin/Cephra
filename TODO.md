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

# Task: Add plate number feature to main-vehicle-card in dashboard.php

## Breakdown of Steps

1. **[x] Modify PHP query to fetch plate_number from users table.**
   - Update the SELECT statement to include plate_number.

2. **[x] Add plate_number to vehicle_data array.**
   - Assign the fetched plate_number to the vehicle_data.

3. **[x] Add Plate Number stat-item in the HTML.**
   - Insert a new div with stat-label "Plate Number" and stat-value displaying the plate_number or "Not Set".

4. **[x] Test and verify the display.**
   - Ensure the plate number shows correctly in the dashboard.
   - Update TODO.md after each step.

Task completed successfully.
