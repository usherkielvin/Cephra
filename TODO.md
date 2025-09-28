# Update Car Display in Dashboard and Link Pages with Database Integration

## Information Gathered
- dashboard.php: Has models array mapping car_index 0-8 to car names, vehicle_specs with hardcoded specs, and car_index check >=0 && <=8. Need to fetch battery_level from battery_levels table.
- link.php: Generates car_index rand(1,10), shows hardcoded "Porsche connected", "Car Model: Porsche $carIndex", image "images/ads.png".
- Database: battery_levels table has battery_level for each username.

## Plan
- [ ] Update dashboard.php: Add query to fetch battery_level from battery_levels table where username = :username. Use this for 'battery_level' in $vehicle_data. Keep range and time_to_full hardcoded for now.
- [ ] Update link.php: Change rand(1,10) to rand(0,8).
- [ ] Add models array in link.php identical to dashboard.php.
- [ ] Change "Porsche connected" to "$model connected".
- [ ] Change "Car Model: Porsche $carIndex" to "Car Model: $model".
- [ ] Add car_images array with placeholder images for each car (e.g., via.placeholder.com).
- [ ] Change image src to $car_images[$carIndex].
- [ ] In dashboard.php, add image in main-vehicle-card: <img src="$car_images[$car_index]" alt="$vehicle_data['model']" style="width:100%; border-radius:20px; margin-bottom:1rem;"> before the vehicle-details.

## Dependent Files
- Appweb/User/dashboard.php
- Appweb/User/link.php

## Followup Steps
- [ ] Test by linking a car and viewing dashboard.
- [ ] Ensure images are properly placed (may need to add car images to images/ folder).
- [ ] Verify battery_level is fetched from DB.
