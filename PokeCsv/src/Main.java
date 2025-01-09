import java.io.*;

public class Main {
    public static void main(String[] args) {
        // I/O Files
        String inputFileName = "pokemon.csv";
        String outputFileName = "pokemon_with_links.csv";

        // Read and write
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {

            String line;
            // Add the links col to the first line
            if ((line = reader.readLine()) != null) {
                writer.write(line + ",Links\n");
            }

            // Get each line
            while ((line = reader.readLine()) != null) {
                String[] lines = line.split(",");
                if (lines.length >= 13) {
                    // Get the Pokemon number and name
                    int number = Integer.parseInt(lines[0]);
                    String name = lines[1];

                    // Generate the link for the Pokemon
                    String link = LinkAdder.generateLink(name, number);

                    // Finally write the original data plus the link
                    writer.write(line + "," + link + "\n");
                } else {
                    System.err.println("Error: Invalid line format: " + line);
                }
            }

            System.out.println("Links added successfully to " + outputFileName + "!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}