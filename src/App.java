import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class App {
    public static void main(String[] args) throws Exception {

        HashMap<String, Integer> hola = sufijos("Marica");

        Map<String, Integer> hello = sorting(hola);

        for (Map.Entry<String, Integer> en : hello.entrySet()) {
            String key = en.getKey();
            Integer val = en.getValue();

            System.out.println(key + " " + val);
            
        }

    }

    public static ArrayList<String> sufijos_recortado(String secuencia){

        secuencia = secuencia.toUpperCase();
        String[] arreglo = secuencia.split("");

        ArrayList<String> arreglo_v2 = new ArrayList<>(Arrays.asList(arreglo));

        return arreglo_v2;
    }

    public static HashMap<String, Integer> sufijos(String secuencia){
        
        secuencia = secuencia.toUpperCase();

        Integer size = secuencia.length();

        HashMap<String, Integer> sufijos = new HashMap<>();

        for (int i = size; i >= 0; i--) {
            sufijos.put(secuencia.substring(i), i);
        }

        return sufijos;
    }

    public static Map<String, Integer> sorting(HashMap<String, Integer> arreglo_sufijos){


        Map<String, Integer> tree_map = new TreeMap<>(arreglo_sufijos);

        return tree_map;
    }

    public static String burrows_weller_transformed(String secuencia){

        String[] arreglo = secuencia.split("");
        Set<String> hash = new HashSet<>();

        return "Hola";
        
    }
}

