package extras;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class URLShortener {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SHORT_URL_LENGTH = 6;
    private final Map<String, String> shortToLongUrl;
    private final Map<String, String> longToShortUrl;
    private final Random random;

    public URLShortener() {
        this.shortToLongUrl = new ConcurrentHashMap<>();
        this.longToShortUrl = new ConcurrentHashMap<>();
        this.random = new Random();
    }

    public String shortenUrl(String longUrl) {
        // Check if URL is already shortened
        if (longToShortUrl.containsKey(longUrl)) {
            return longToShortUrl.get(longUrl);
        }

        // Generate a unique short URL
        String shortUrl;
        do {
            shortUrl = generateShortUrl();
        } while (shortToLongUrl.containsKey(shortUrl));

        // Store the mapping
        shortToLongUrl.put(shortUrl, longUrl);
        longToShortUrl.put(longUrl, shortUrl);

        return shortUrl;
    }

    public String getLongUrl(String shortUrl) {
        return shortToLongUrl.get(shortUrl);
    }

    private String generateShortUrl() {
        StringBuilder shortUrl = new StringBuilder();
        for (int i = 0; i < SHORT_URL_LENGTH; i++) {
            int index = random.nextInt(ALPHABET.length());
            shortUrl.append(ALPHABET.charAt(index));
        }
        return shortUrl.toString();
    }

    // Main method for testing
    public static void main(String[] args) {
        URLShortener shortener = new URLShortener();

        // Test URLs
        String[] testUrls = {
                "https://www.example.com/very/long/url/that/needs/shortening",
                "https://www.google.com/search?q=very+long+search+query+with+many+parameters",
                "https://www.github.com/username/repository/very/long/path"
        };

        for (String url : testUrls) {
            String shortUrl = shortener.shortenUrl(url);
            System.out.println("Original URL: " + url);
            System.out.println("Shortened URL: " + shortUrl);
            System.out.println("Retrieved URL: " + shortener.getLongUrl(shortUrl));
            System.out.println("-------------------");
        }
    }
}
