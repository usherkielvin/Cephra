// Simple Service Worker for Cephra
const CACHE_NAME = 'cephra-v1';
const urlsToCache = [
    '/',
    'dashboard.php',
    'css/main.css',
    'assets/css/fontawesome-all.min.css',
    'images/logo.png'
];

self.addEventListener('install', function(event) {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(function(cache) {
                return cache.addAll(urlsToCache);
            })
    );
});

self.addEventListener('fetch', function(event) {
    event.respondWith(
        caches.match(event.request)
            .then(function(response) {
                if (response) {
                    return response;
                }
                return fetch(event.request);
            }
        )
    );
});
