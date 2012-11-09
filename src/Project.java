import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * @author Sergio Silva 55457
 * @author Marco Ferreira 56886
 */
public class Project {

    private static long _hashRPrime1 = 3;
    private static long _hashRPrime2 = 96293;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try (
                BufferedReader inReader = new BufferedReader(new FileReader(args[1]));
                BufferedReader br = new BufferedReader(new FileReader(args[0]));
                BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"))
        ) {
            String input;
            String[] options;
            while ((input = inReader.readLine()) != null) {
                options = input.split(" ");
                switch (options[0]) {
                    case "N": {
                        String protLine;
                        String sequence;
                        String ref;
                        try {
                            while ((protLine = br.readLine()) != null) {
                                ref = protLine.substring(0, 14);
                                sequence = processSequence(br);
                                if (naiveMatcher(sequence, options[1])) {
                                    out.write(ref);
                                }
                            }
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;
                    }
                    case "R": {
                        String protLine;
                        String sequence;
                        String ref;
                        try {
                            while ((protLine = br.readLine()) != null) {
                                ref = protLine.substring(0, 14);
                                sequence = processSequence(br);
                                if (rabinKarpMatcher(sequence, options[1])) {
                                    out.write(ref);
                                }
                            }
                            br.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "Q": {
                        String protLine;
                        String sequence;
                        String ref;
                        String refHistory = "";
                        int seqSize;
                        try {
                            while ((protLine = br.readLine()) != null) {
                                ref = protLine.substring(0, 14);
                                sequence = processSequence(br);
                                for ( seqSize = 1 ; seqSize < options[1].length() + 1 ; seqSize *= 2 ) {
                                    if (rabinKarpMatcher(sequence, options[1].substring(0, seqSize)) && !ref.equalsIgnoreCase(refHistory)) {
                                        out.write(ref);
                                    }
                                }
                                refHistory = ref;
                            }
                            br.close();
                        } catch (Exception e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;
                    }

                    case "L": {
                        break;
                    }

                    default: {
                        return;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static boolean naiveMatcher(String T, String P) {
        try {
            int n = T.length();
            int m = P.length();
            for (int i = 0; i < n - m; i++) {
                if (P.compareTo(T.substring(i, i + m)) == 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    private static boolean rabinKarpMatcher(String T, String P) {
        try {
            int n = T.length();
            int m = P.length();
            long patternHash = hashR(P, _hashRPrime1, _hashRPrime2);
            long textHash = hashR(T.substring(0, m), _hashRPrime1, _hashRPrime2);
            char[] textArray = T.toCharArray();
            long bmModM = preCompute(_hashRPrime1, m);
            for (int i = 0; i < n - m; i++) {
                if (textHash == patternHash) {
                    System.out.println("pattern encontrado");
                    if (T.substring(i, i + m).equalsIgnoreCase(P)) {
                        return true;
                    }
                }
                textHash -= textArray[i] * bmModM;
                while (textHash < 0) {
                    textHash += _hashRPrime2;
                }
                textHash = (textHash * _hashRPrime1 + (textArray[i + m])) % _hashRPrime2;

            }
            return false;
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return false;
    }

    private static String processSequence(BufferedReader br) {
        try {
            StringBuilder resSequence = new StringBuilder();
            String seqLine;
            while (!"".equals(seqLine = br.readLine())) {
                if (seqLine != null) {
                    resSequence.append(seqLine);
                } else {
                    return resSequence.toString();
                }
            }
            return resSequence.toString();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return "";
    }

    private static long hashR(String P, long b, long M) {
        long h = 0;
        char[] p = P.toCharArray();
        for (int i = 0; i < P.length(); i++) {
            h *= b;
            h += (int) p[i];
            h %= M;
        }
        return h;
    }

    public static void Erathosthenes(int N) {

        boolean[] isPrime = new boolean[N + 1];
        for (int i = 2; i <= N; i++)
            isPrime[i] = true;

        for (int i = 2; i * i <= N; i++) {
            if (isPrime[i]) {
                for (int j = i; i * j <= N; j++)
                    isPrime[i * j] = false;
            }
        }
        for (int i = 2; i <= N; i++) {
            if (isPrime[i])
                System.out.println(" " + i);
        }
    }

    private static long preCompute(long b, int m) {
        if (m == 1) {
            return 1;
        }
        if (m == 2) {
            return b;
        }
        long ret = 1;
        for (int i = 1; i < m; i++) {
            ret *= b;
        }
        return ret;
    }

}
