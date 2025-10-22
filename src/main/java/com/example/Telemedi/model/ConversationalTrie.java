package com.example.Telemedi.model;

import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class ConversationalTrie {
    private TrieNode root;

    public ConversationalTrie() {
        this.root = new TrieNode("Hello! I'm your medical how are you today? feeling well?");
        buildMedicalDialogue();
    }

    private void buildMedicalDialogue() {
        // Headache branch
        TrieNode headacheNode = new TrieNode("You mentioned a headache. Can you describe the type of pain?");
        headacheNode.setSuggestions(Arrays.asList("throbbing", "dull", "sharp", "pressure"));
        root.addChild("headache", headacheNode);
        root.addChild("head", headacheNode);

        // Throbbing headache
        TrieNode throbbingNode = new TrieNode("A throbbing headache noted. Do you experience sensitivity to light or sound?");
        throbbingNode.setSuggestions(Arrays.asList("yes", "no", "light sensitive", "sound sensitive"));
        headacheNode.addChild("throbbing", throbbingNode);

        TrieNode migraineLikelyNode = new TrieNode("This sounds like a migraine headache.");
        migraineLikelyNode.setRemedy("Take Sumatriptan 50mg or Ibuprofen 400mg. Rest in a dark, quiet room. Apply cold compress to forehead.");
        migraineLikelyNode.setEndNode(true);
        throbbingNode.addChild("yes", migraineLikelyNode);
        throbbingNode.addChild("light", migraineLikelyNode);

        TrieNode tensionHeadacheNode = new TrieNode("This appears to be a tension headache.");
        tensionHeadacheNode.setRemedy("Take Paracetamol 650mg or Dolo 650. Apply warm compress. Practice relaxation techniques.");
        tensionHeadacheNode.setEndNode(true);
        throbbingNode.addChild("no", tensionHeadacheNode);

        // Dull headache
        TrieNode dullHeadacheNode = new TrieNode("A dull headache can indicate tension or dehydration.");
        dullHeadacheNode.setRemedy("Take Paracetamol 500mg. Drink plenty of water. Get adequate rest. Practice neck stretches.");
        dullHeadacheNode.setEndNode(true);
        headacheNode.addChild("dull", dullHeadacheNode);

        // Sharp headache
        TrieNode sharpHeadacheNode = new TrieNode("Sharp, sudden headaches can be concerning.");
        sharpHeadacheNode.setRemedy("Take Ibuprofen 400mg. If severe or persistent, consult a doctor immediately.");
        sharpHeadacheNode.setEndNode(true);
        headacheNode.addChild("sharp", sharpHeadacheNode);

        // Fever branch
        TrieNode feverNode = new TrieNode("You have a fever. What is your temperature range?");
        feverNode.setSuggestions(Arrays.asList("high fever", "low fever", "moderate", "above 102", "below 100"));
        root.addChild("fever", feverNode);
        root.addChild("temperature", feverNode);

        TrieNode highFeverNode = new TrieNode("High fever requires immediate attention.");
        highFeverNode.setRemedy("Take Paracetamol 500mg every 6 hours. Sponge with lukewarm water. Drink fluids. See doctor if above 102°F.");
        highFeverNode.setEndNode(true);
        feverNode.addChild("high", highFeverNode);
        feverNode.addChild("above", highFeverNode);

        TrieNode lowFeverNode = new TrieNode("Low-grade fever is usually manageable at home.");
        lowFeverNode.setRemedy("Take Dolo 650mg. Rest well. Drink warm liquids. Monitor temperature regularly.");
        lowFeverNode.setEndNode(true);
        feverNode.addChild("low", lowFeverNode);
        feverNode.addChild("below", lowFeverNode);

        // Cough branch
        TrieNode coughNode = new TrieNode("You have a cough. Is it a dry cough or are you bringing up phlegm?");
        coughNode.setSuggestions(Arrays.asList("dry cough", "wet cough", "with phlegm", "no phlegm"));
        root.addChild("cough", coughNode);
        root.addChild("coughing", coughNode);

        TrieNode dryCoughNode = new TrieNode("A dry cough can be irritating and persistent.");
        dryCoughNode.setRemedy("Take Dextromethorphan syrup 10ml three times daily. Drink honey with warm water. Avoid cold beverages.");
        dryCoughNode.setEndNode(true);
        coughNode.addChild("dry", dryCoughNode);
        coughNode.addChild("no", dryCoughNode);

        TrieNode wetCoughNode = new TrieNode("A productive cough with phlegm indicates your body is clearing mucus.");
        wetCoughNode.setRemedy("Take Bromhexine syrup 10ml three times daily. Do steam inhalation twice daily. Drink plenty of warm fluids.");
        wetCoughNode.setEndNode(true);
        coughNode.addChild("wet", wetCoughNode);
        coughNode.addChild("with", wetCoughNode);
        coughNode.addChild("phlegm", wetCoughNode);

        // Stomach pain branch
        TrieNode stomachNode = new TrieNode("You're experiencing stomach pain. Is it cramping, burning, or sharp pain?");
        stomachNode.setSuggestions(Arrays.asList("cramping", "burning", "sharp pain", "bloating"));
        root.addChild("stomach", stomachNode);
        root.addChild("stomachache", stomachNode);
        root.addChild("abdominal", stomachNode);

        TrieNode crampingNode = new TrieNode("Stomach cramping often indicates gas or indigestion.");
        crampingNode.setRemedy("Take Digene tablet or ENO. Avoid spicy foods. Drink buttermilk. Apply warm compress to abdomen.");
        crampingNode.setEndNode(true);
        stomachNode.addChild("cramping", crampingNode);
        stomachNode.addChild("bloating", crampingNode);

        TrieNode burningNode = new TrieNode("A burning sensation suggests acidity or heartburn.");
        burningNode.setRemedy("Take Omeprazole 20mg before meals. Avoid citrus fruits and spicy food. Eat small, frequent meals.");
        burningNode.setEndNode(true);
        stomachNode.addChild("burning", burningNode);

        TrieNode sharpStomachNode = new TrieNode("Sharp abdominal pain can indicate various conditions.");
        sharpStomachNode.setRemedy("Take Buscopan for spasms. If severe or persistent, consult a doctor immediately.");
        sharpStomachNode.setEndNode(true);
        stomachNode.addChild("sharp", sharpStomachNode);

        // Body ache branch
        TrieNode bodyAcheNode = new TrieNode("Body aches can be due to various reasons. Do you also have fever?");
        bodyAcheNode.setSuggestions(Arrays.asList("with fever", "no fever", "muscle pain", "joint pain"));
        root.addChild("body", bodyAcheNode);
        root.addChild("bodyache", bodyAcheNode);
        root.addChild("ache", bodyAcheNode);

        TrieNode bodyAcheFeverNode = new TrieNode("Body ache with fever suggests viral infection.");
        bodyAcheFeverNode.setRemedy("Take Paracetamol 650mg for pain and fever. Rest well. Drink plenty of fluids. Monitor symptoms.");
        bodyAcheFeverNode.setEndNode(true);
        bodyAcheNode.addChild("with", bodyAcheFeverNode);
        bodyAcheNode.addChild("fever", bodyAcheFeverNode);

        TrieNode bodyAcheNoFeverNode = new TrieNode("Body ache without fever might be due to exertion or stress.");
        bodyAcheNoFeverNode.setRemedy("Take Ibuprofen 400mg. Apply warm compress to sore areas. Do gentle stretching exercises.");
        bodyAcheNoFeverNode.setEndNode(true);
        bodyAcheNode.addChild("no", bodyAcheNoFeverNode);
        bodyAcheNode.addChild("muscle", bodyAcheNoFeverNode);
    }

    public TrieNode getRoot() {
        return root;
    }

    public TrieNode findBestMatch(TrieNode currentNode, String userInput) {
        if (userInput == null || userInput.trim().isEmpty()) {
            return null;
        }

        String input = userInput.toLowerCase().trim();
        String[] words = input.split("\\s+");

        // First try exact phrase match
        TrieNode exactMatch = currentNode.getChild(input);
        if (exactMatch != null) {
            return exactMatch;
        }

        // Then try multi-word combinations
        for (int i = words.length; i > 0; i--) {
            for (int j = 0; j <= words.length - i; j++) {
                StringBuilder phrase = new StringBuilder();
                for (int k = j; k < j + i; k++) {
                    if (k > j) phrase.append(" ");
                    phrase.append(words[k]);
                }
                TrieNode match = currentNode.getChild(phrase.toString());
                if (match != null) {
                    return match;
                }
            }
        }

        // Then try individual words (existing logic)
        for (String word : words) {
            TrieNode match = currentNode.getChild(word);
            if (match != null) {
                return match;
            }
        }

        // Fuzzy matching as fallback
        for (String word : words) {
            for (String keyword : currentNode.getChildren().keySet()) {
                if (isCloseMatch(word, keyword)) {
                    return currentNode.getChild(keyword);
                }
            }
        }

        return null;
    }

    private boolean isCloseMatch(String word1, String word2) {
        return word1.contains(word2) || word2.contains(word1) ||
                calculateLevenshteinDistance(word1, word2) <= 2;
    }

    private int calculateLevenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= s2.length(); j++) dp[0][j] = j;

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                if (s1.charAt(i-1) == s2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i-1][j], dp[i][j-1]), dp[i-1][j-1]);
                }
            }
        }

        return dp[s1.length()][s2.length()];
    }
}
