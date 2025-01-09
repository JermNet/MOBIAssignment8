public class LinkAdder {
    public static String generateLink(String name, int number) {
        // Create the base link without a number
        StringBuilder link = new StringBuilder("https://www.serebii.net/xy/pokemon/");
        // Add 1 or 2 leading zeros depending on the number
        link.append(String.format("%03d", number));

        // If the name contains "mega", then we get the mega evolution image (also checking for edge case pokemon)
        if (name.toLowerCase().contains("mega") && !name.equalsIgnoreCase("Meganium") && !name.equalsIgnoreCase("Yanmega")) {
            link.append("-m");
            // Then check for Mewtwo or Charizard since they have 2 mega evolutions, so they don't have"-m" they have "-mx" and "-my" links
            if (name.equalsIgnoreCase("CharizardMega Charizard X")||name.equalsIgnoreCase("MewtwoMega Mewtwo X")) {
                link.append("x");
            } else if (name.equalsIgnoreCase("CharizardMega Charizard Y")||name.equalsIgnoreCase("MewtwoMega Mewtwo Y")) {
                link.append("y");
            }
        }
        // Check for primal Pokemon (there are only two because why not?)
        else if (name.toLowerCase().contains("primal")) {
            link.append("-p");
        }
        // Pumpkaboo check
        else if (name.toLowerCase().contains("small")) {
            link.append("-s");
        }
        else if (name.toLowerCase().contains("large")) {
            link.append("-l");
        }
        else if (name.toLowerCase().contains("super")) {
            link.append("-h");
        }
        // All of the other forms use the same image so there aren't a million edge cases. I could have done this based of the Pokemon
        // dex number, but you can't get the form based off of just that so I did it this way.
        else if (name.toLowerCase().contains("forme")) {
            // Do nothing zone
        }
        // Finally add the file extension before returning
        link.append(".png");
        return link.toString();
    }
}
