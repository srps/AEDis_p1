import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * @author Sergio Silva 55457
 * @author Marco Ferreira 56886
 */
public class Project {

    private static long _hashRPrime1 = 2;
    private static long _hashRPrime2 = 96293;
    private int tempB = 3;
    private int tempM = 96293;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try (
                BufferedReader inReader = new BufferedReader(new FileReader(args[1]));

                BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"))
        ) {
            String input;
            String[] options;
            while ((input = inReader.readLine()) != null) {
                options = input.split(" ");
                try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
                    switch (options[0]) {
                        case "N": {
                            String protLine;
                            String sequence;
                            String ref;
                            try {
                                while ((protLine = br.readLine()) != null) {
                                    ref = protLine.substring(0, protLine.indexOf("ref"));
                                    sequence = processSequence(br);
                                    if (naiveMatcher(sequence, options[1])) {
                                        out.write(ref);
                                        out.newLine();
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
                                    ref = protLine.substring(0, protLine.indexOf("ref"));
                                    sequence = processSequence(br);
                                    if (rabinKarpMatcher(sequence, options[1])) {
                                        out.write(ref);
                                        out.newLine();
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "Q": {
                            String protLine;
                            String sequence;
                            String ref;
                            ArrayList<String> result = new ArrayList<>();
                            int resultSize = 0;
                            int maxPatternSize = 0;
                            int patternSize;
                            int begin, end;

                            try {
                                while ((protLine = br.readLine()) != null) {
                                    ref = protLine.substring(0, protLine.indexOf("ref"));
                                    sequence = processSequence(br);
                                    System.out.println(options[1]);
                                    for (begin = 0; begin < options[1].length(); begin++) {
                                        for (end = begin + 1; end < options[1].length() + 1; end++) {
                                            //System.out.println(options[1].substring(begin, end));
                                            if (rabinKarpMatcher(sequence, options[1].substring(begin, end))) {
                                                patternSize = options[1].substring(begin, end).length();
                                                if (patternSize > maxPatternSize) {
                                                    maxPatternSize = patternSize;
                                                }
                                            }
                                        }
                                    }
                                    if (maxPatternSize > resultSize) {
                                        // Remover ArrayList caso lá tenha qualquer coisa
                                        if (resultSize != 0) {
                                            result.clear();
                                        }
                                        // Adicionar novo
                                        result.add(ref);
                                        resultSize = maxPatternSize;
                                    } else {
                                        //Se for igual adicionar
                                        if (maxPatternSize == resultSize) {
                                            result.add(ref);
                                        }
                                    }
                                    maxPatternSize = 0;
                                }
                                // Print results
                                for (String s : result) {
                                    out.write(s);
                                    out.newLine();
                                }
                            } catch (Exception e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                            break;
                        }

                        case "L": {
                            String protLine;
                            String sequence;
                            String ref;
                            ArrayList<String> result = new ArrayList<>();
                            int resultSize = 0;
                            int i;
                            int maxPatternSize = 0;
                            int patternSize;
                            int begin, end;

                            try {
                                while ((protLine = br.readLine()) != null) {
                                    ref = protLine.substring(0, protLine.indexOf("ref"));
                                    sequence = processSequence(br);
                                    for (begin = 0; begin < options[1].length(); begin++) {
                                        System.out.println("BU");
                                        i = 1;
                                        for (end = begin + i; end < options[1].length() + 1; i *= 2, end += i) {
                                            //System.out.println(end);
                                            //System.out.println(options[1].substring(seqSize, seqSize2));
                                            if (rabinKarpMatcher(sequence, options[1].substring(begin, end))) {
                                                patternSize = options[1].substring(begin, end).length();
                                                if (patternSize > maxPatternSize) {
                                                    maxPatternSize = patternSize;
                                                }
                                            }
                                        }
                                    }
                                    if (maxPatternSize > resultSize) {
                                        // Remover ArrayList caso lá tenha qualquer coisa
                                        if (resultSize != 0) {
                                            result.clear();
                                        }
                                        // Adicionar novo
                                        result.add(ref);
                                        resultSize = maxPatternSize;
                                    } else {
                                        //Se for igual adicionar
                                        if (maxPatternSize == resultSize) {
                                            result.add(ref);
                                        }
                                    }
                                    maxPatternSize = 0;
                                }
                                // Print results
                                for (String s : result) {
                                    out.write(s);
                                    out.newLine();
                                }
                            } catch (Exception e) {
                                System.err.println("Error: " + e.getMessage());
                            }
                            break;
                        }
                        default: {
                            return;
                        }
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
                    //System.out.println("p : " + T.substring(i, i+m));
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

    private static int powerTwo(int exponent) {
        int result = 1;

        if (exponent == 0) return 1;
        else {
            for (; exponent > 0; exponent--) {
                result *= 2;
            }
        }
        return result;
    }
}