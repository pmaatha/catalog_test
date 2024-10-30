import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class shamirSecretsharing {

    // Function to decode a value from a given base
    private static BigInteger decodeValue(String base, String value) {
        return new BigInteger(value, Integer.parseInt(base));
    }

    // Function to compute the constant term using Lagrange interpolation
    private static BigInteger lagrangeInterpolation(List<BigInteger> xValues, List<BigInteger> yValues) {
        BigInteger total = BigInteger.ZERO;
        int k = xValues.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = xValues.get(i);
            BigInteger yi = yValues.get(i);
            BigInteger li = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = xValues.get(j);
                    li = li.multiply(BigInteger.ZERO.subtract(xj)).divide(xi.subtract(xj));
                }
            }
            total = total.add(li.multiply(yi));
        }
        return total;
    }

    // Main function to read JSON input and compute the constant term
    private static BigInteger computeSecretFromJson(JSONObject inputJson) {
        JSONObject keys = inputJson.getJSONObject("keys");
        int n = keys.getInt("n");
        int k = keys.getInt("k");

        List<BigInteger> xValues = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        // Decode the values from the JSON input
        for (int i = 1; i <= n; i++) {
            // Ensure that the key exists before attempting to access it
            if (inputJson.has(String.valueOf(i))) {
                JSONObject root = inputJson.getJSONObject(String.valueOf(i));
                String base = root.getString("base");
                String value = root.getString("value");

                xValues.add(BigInteger.valueOf(i)); // x = i
                yValues.add(decodeValue(base, value)); // Decode y value
            } else {
                System.out.println("Key " + i + " not found in JSON.");
            }
        }

        // Ensure we only use the first k points for interpolation
        BigInteger secretC = lagrangeInterpolation(xValues.subList(0, k), yValues.subList(0, k));

        return secretC;
    }

    public static void main(String[] args) {
        // Test case 1 JSON input
        String jsonString1 = "{\n" +
                "    \"keys\": {\n" +
                "        \"n\": 4,\n" +
                "        \"k\": 3\n" +
                "    },\n" +
                "    \"1\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"4\"\n" +
                "    },\n" +
                "    \"2\": {\n" +
                "        \"base\": \"2\",\n" +
                "        \"value\": \"111\"\n" +
                "    },\n" +
                "    \"3\": {\n" +
                "        \"base\": \"10\",\n" +
                "        \"value\": \"12\"\n" +
                "    },\n" +
                "    \"6\": {\n" +
                "        \"base\": \"4\",\n" +
                "        \"value\": \"213\"\n" +
                "    }\n" +
                "}";

        // Test case 2 JSON input
        String jsonString2 = "{\n" +
                "  \"keys\": {\n" +
                "    \"n\": 10,\n" +
                "    \"k\": 7\n" +
                "  },\n" +
                "  \"1\": {\n" +
                "    \"base\": \"6\",\n" +
                "    \"value\": \"13444211440455345511\"\n" +
                "  },\n" +
                "  \"2\": {\n" +
                "    \"base\": \"15\",\n" +
                "    \"value\": \"aed7015a346d63\"\n" +
                "  },\n" +
                "  \"3\": {\n" +
                "    \"base\": \"15\",\n" +
                "    \"value\": \"6aeeb69631c227c\"\n" +
                "  },\n" +
                "  \"4\": {\n" +
                "    \"base\": \"16\",\n" +
                "    \"value\": \"e1b5e05623d881f\"\n" +
                "  },\n" +
                "  \"5\": {\n" +
                "    \"base\": \"8\",\n" +
                "    \"value\": \"316034514573652620673\"\n" +
                "  },\n" +
                "  \"6\": {\n" +
                "    \"base\": \"3\",\n" +
                "    \"value\": \"2122212201122002221120200210011020220200\"\n" +
                "  },\n" +
                "  \"7\": {\n" +
                "    \"base\": \"3\",\n" +
                "    \"value\": \"20120221122211000100210021102001201112121\"\n" +
                "  },\n" +
                "  \"8\": {\n" +
                "    \"base\": \"6\",\n" +
                "    \"value\": \"20220554335330240002224253\"\n" +
                "  },\n" +
                "  \"9\": {\n" +
                "    \"base\": \"12\",\n" +
                "    \"value\": \"45153788322a1255483\"\n" +
                "  },\n" +
                "  \"10\": {\n" +
                "    \"base\": \"7\",\n" +
                "    \"value\": \"1101613130313526312514143\"\n" +
                "  }\n" +
                "}";

        // Process test case 1
        JSONObject inputJson1 = new JSONObject(jsonString1);
        BigInteger secretC1 = computeSecretFromJson(inputJson1);
        System.out.println("Test Case 1 - The constant term (secret) is: " + secretC1);

        // Process test case 2
        JSONObject inputJson2 = new JSONObject(jsonString2);
        BigInteger secretC2 = computeSecretFromJson(inputJson2);
        System.out.println("Test Case 2 - The constant term (secret) is: " + secretC2);
    }
}