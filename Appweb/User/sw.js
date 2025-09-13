const CACHE_NAME = 'cephra-pwa-v1';
const OFFLINE_URL = 'offline.html';
const ASSETS = [
  '/',
  'dashboard.php',
  'ChargingPage.php',
  'link.php',
  'history.php',
  'profile.php',
  'assets/css/main.css',
  'assets/js/main.js',
  'assets/js/jquery.min.js',
  'assets/js/jquery.dropotron.min.js',
  'assets/js/browser.min.js',
  'assets/js/breakpoints.min.js',
  'assets/js/util.js',
  'images/logo.png',
  OFFLINE_URL
];

self.addEventListener('install', (event) => {
  event.waitUntil(
    caches.open(CACHE_NAME).then((cache) => cache.addAll(ASSETS))
  );
  self.skipWaiting();
});

self.addEventListener('activate', (event) => {
  event.waitUntil(
    caches.keys().then((keys) => Promise.all(keys.map((k) => k !== CACHE_NAME && caches.delete(k))))
  );
  self.clients.claim();
});

self.addEventListener('fetch', (event) => {
  const { request } = event;
  if (request.method !== 'GET') return;
  event.respondWith(
    caches.match(request).then((cached) => {
      const fetchPromise = fetch(request).then((response) => {
        const copy = response.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(request, copy)).catch(() => {});
        return response;
      }).catch(() => cached || caches.match(OFFLINE_URL));
      return cached || fetchPromise;
    })
  );
});

