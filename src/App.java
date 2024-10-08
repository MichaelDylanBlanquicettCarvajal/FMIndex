
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;

public class App {

    public static void main(String[] args) throws Exception {

        String palabra = "abaaba$";
        HashMap<String, Integer> hola = sufijos(palabra);

        TreeMap<String, Integer> hello = sorting(hola);

        for (Map.Entry<String, Integer> en : hello.entrySet()) {
            String key = en.getKey();
            Integer val = en.getValue();

            System.out.println(key + " " + val);

        }

        //System.out.println(burrows_weller_transformed(palabra));

        //System.out.println(busqueda_binaria(hello, "I", hello.firstKey(), hello.lastKey()));

        //System.out.println(leer_fasta("data/sequence.fasta"));
        //System.out.println(leer_fastq("data/ERR10088338.fastq.gz"));

        List<String[]> lf_index = LF_index(hello, palabra);
        
        Map<String, List<Integer>> tally = calcular_tally_values(lf_index);

        for (String[] arreglo : lf_index) {
            System.out.println(Arrays.toString(arreglo));
        }


        Map<String, List<Integer>> tallyValues = calcular_tally_values(lf_index);
        for (Map.Entry<String, List<Integer>> entry : tallyValues.entrySet()) {
            System.out.println("Carácter: " + entry.getKey() + " -> " + entry.getValue());
        }

        probarBusquedaSubcadena(tally, lf_index, "BA");
        
    }

    public static void probarBusquedaSubcadena(Map<String, List<Integer>> tally, List<String[]> lfIndex, String subcadena) {
        Object[] resultado = buscar_subcadena_indice_FM(tally, lfIndex, subcadena);
        if ((boolean) resultado[0]) {
            System.out.println("Subcadena '" + subcadena + "' encontrada en la posición del sufijo: " + resultado[1]);
        } else {
            System.out.println("Subcadena '" + subcadena + "' no encontrada.");
        }
    }


    public static HashMap<String, Integer> sufijos(String secuencia) {

        secuencia = secuencia.toUpperCase();

        Integer size = secuencia.length();

        HashMap<String, Integer> sufijos = new HashMap<>();

        for (int i = size - 1; i >= 0; i--) {
            sufijos.put(secuencia.substring(i), i);
        }

        return sufijos;
    }

    public static TreeMap<String, Integer> sorting(HashMap<String, Integer> arreglo_sufijos) {

        TreeMap<String, Integer> tree_map = new TreeMap<>(arreglo_sufijos);

        return tree_map;
    }

    private static Integer busqueda_binaria(TreeMap<String, Integer> sufijos, String subcadena, String inicio, String fin) {
        if (inicio == null || fin == null || inicio.equals(fin)) {
            return null; // No se encontró la subcadena
        }

        // Encontrar la clave media
        int size = sufijos.subMap(inicio, true, fin, true).size();
        int midIndex = size / 2;
        String medio = null;
        int count = 0;

        for (String key : sufijos.subMap(inicio, true, fin, true).keySet()) {
            if (count == midIndex) {
                medio = key;
                break;
            }
            count++;
        }

        if (medio == null) {
            return null; // No se encontró la subcadena
        }

        if (medio.startsWith(subcadena)) {
            return sufijos.get(medio);
        } 
        else if (inicio.startsWith(subcadena)){
            return sufijos.get(inicio);
        }
        else if (fin.startsWith(subcadena)){
            return sufijos.get(fin);
        }
        else if (medio.compareTo(subcadena) < 0) {
            return busqueda_binaria(sufijos, subcadena, sufijos.higherKey(medio), fin);
        } else {
            return busqueda_binaria(sufijos, subcadena, inicio, sufijos.lowerKey(medio));
        }
    }
    
    public static String burrows_weller_transformed(String secuencia) {

        String bw = "";
        String[] arreglo_secuencia = secuencia.toUpperCase().split("");

        Map<String, Integer> lista_sufijos_ordenada = sorting(sufijos(secuencia));

        for (Map.Entry<String, Integer> en : lista_sufijos_ordenada.entrySet()) {
            Integer val = en.getValue();

            if (val == 0) {
                bw = bw + arreglo_secuencia[secuencia.length() - 1];
            } else {
                bw = bw + arreglo_secuencia[val - 1];
            }
        }

        return bw;

    }

    //[F, L, SA]
    public static List<String[]> LF_index(TreeMap<String, Integer> sufijos_ordenados, String secuencia){

        List<String[]> LF = new ArrayList<>();
        String[] arreglo = secuencia.toUpperCase().split("");

        String[] linea;
        
        for (Map.Entry<String, Integer> en : sufijos_ordenados.entrySet()) {
            linea = new String[3];
            String key = en.getKey();
            Integer val = en.getValue();
            linea[0]=key.split("")[0];
            if(val != 0){
                linea[1]=arreglo[val-1];
            }
            else{
                linea[1]=arreglo[secuencia.length()-1];
            }
            linea[2]=Integer.toString(val);
            LF.add(linea);
        }

        return LF;
    }

    public static Map<String, List<Integer>> calcular_tally_values(List<String[]> lf_index) {

        // Inicializar el mapa para los valores de tally
        Map<String, List<Integer>> tallyValues = new HashMap<>();
        Map<String, Integer> currentCounts = new HashMap<>();

        // Inicializar los conteos a cero
        for (String[] entry : lf_index) {
            String lastValue = entry[1];
            if (!currentCounts.containsKey(lastValue)) {
                currentCounts.put(lastValue, 0);
                tallyValues.put(lastValue, new ArrayList<>());
            }
        }

        // Recorrer el índice LF y actualizar los conteos
        for (int i = 0; i < lf_index.size(); i++) {
            String lastValue = lf_index.get(i)[1];

            // Incrementar el conteo del carácter actual antes de actualizar los valores de tally
            currentCounts.put(lastValue, currentCounts.get(lastValue) + 1);

            // Actualizar los valores de tally
            for (Map.Entry<String, Integer> countEntry : currentCounts.entrySet()) {
                String character = countEntry.getKey();
                int count = countEntry.getValue();
                tallyValues.get(character).add(count);
            }
        }

        return tallyValues;
    }

    public static Integer LF_mapping(Map<String, List<Integer>> tally, List<String[]> lfIndex, Integer posicion) {
        if (posicion == null || posicion < 0 || posicion >= lfIndex.size()) {
            return -1; // Posición inválida
        }

        // Obtener el carácter en la posición dada del índice LF
        String lastValue = lfIndex.get(posicion)[1];

        // Obtener el tally count para el carácter en la posición dada
        int tallyCount = tally.get(lastValue).get(posicion);

        // Calcular la nueva posición en el índice LF
        int nuevaPosicion = tallyCount + obtener_primer_columna(lastValue, lfIndex);

        return nuevaPosicion;
    }

    private static int obtener_primer_columna(String character, List<String[]> lfIndex) {
        int count = 0;
        for (String[] entry : lfIndex) {
            if (entry[0].equals(character)) {
                return count;
            }
            count++;
        }
        return -1; // No se encontró el carácter en la primera columna
    }

    public static Object[] buscar_subcadena_indice_FM(Map<String, List<Integer>> tally, List<String[]> lfIndex, String subcadena) {
        for (int start = 0; start < lfIndex.size(); start++) {
            int posicion = start;
            

            boolean encontrado = true;

            for (int i = subcadena.length() - 1; i >= 0; i--) {
                posicion = LF_mapping(tally, lfIndex, posicion);
                if (posicion <= 0 || !lfIndex.get(posicion-1)[1].equals(String.valueOf(subcadena.charAt(i)))) {
                    encontrado = false;
                    break;
                }
            }

            if (encontrado) {
                // Retornar la posición del sufijo donde se encontró la subcadena
                return new Object[]{true, Integer.valueOf(lfIndex.get(posicion)[2])};
            }
        }

        return new Object[]{false, -1}; // No se encontró la subcadena
    }
    public static Set<String> caracteres_diferentes(String secuencia) {

        Set<String> letras_unicas = new TreeSet<>();

        String[] arreglo = secuencia.replace("$", "").split("");

        for(String c: arreglo) {
            letras_unicas.add(c);
        }

        return letras_unicas;
    }

    public static String leer_fasta(String dirección_archivo) {
        String fasta = "";

        try (BufferedReader br = new BufferedReader(new FileReader(dirección_archivo))) {

            String linea = br.readLine();

            while (linea != null) {
                if (!linea.contains(">") || !linea.contains(",")) {
                    fasta = fasta + linea;
                    linea = br.readLine();
                } else {
                    linea = br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fasta;
    }

    public static List<String> leer_fastq(String direccion_archivo) {
        List<String> fastq = new ArrayList<>();

        if(direccion_archivo.contains(".gz")){
            try (GZIPInputStream gz = new GZIPInputStream(new FileInputStream(direccion_archivo));
            BufferedReader br = new BufferedReader(new InputStreamReader(gz))) {
                
                String fastq_read = "";
                String linea = br.readLine();
    
                while (linea != null) { 
                    if(linea.contains("@")){
                        linea = br.readLine();
                        while(!linea.contains("+")){
                            fastq_read = fastq_read + linea;
                            linea = br.readLine();
                        }
                    }
                    fastq.add(fastq_read);
                    fastq_read = "";
                    linea = br.readLine();
                }
    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try (BufferedReader br = new BufferedReader(new FileReader(direccion_archivo))) {
                String fastq_read = "";
                String linea = br.readLine();
    
                while (linea != null) { 
                    if(linea.contains("@")){
                        linea = br.readLine();
                        while(!linea.contains("+")){
                            fastq_read = fastq_read + linea;
                            linea = br.readLine();
                        }
                    }
                    fastq.add(fastq_read);
                    fastq_read = "";
                    linea = br.readLine();
                }
    
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fastq;
    }
}
