package csd_assignment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SocialMedia {
    private Map<String,Person>people = new HashMap<>();


    public SocialMedia() {}

    public SocialMedia(Map<String,Person> H) {
        this.people = H;
    }

   public void addPerson(Person person) {
        if (people.containsKey(person.getId())) {
            System.out.println("Person with ID " + person.getId() + " already exists. Cannot add duplicate.");
        } else {
            people.put(person.getId(), person);
            System.out.println("Person added successfully: " + person.getName());
        }
    }


    public Person getPerson(int id) {
        return people.get(id);
    }

    // Finding potential friends based on weights
    public List<Person> findPotentialFriends(Person person) {
        Map<Person, Integer> potentialFriends = new HashMap<>();

        // 1. Friend of a friend (weight = 3)
        for (Person friend : person.getFriends()) {
            for (Person friendOfFriend : friend.getFriends()) {
                if (friendOfFriend != person && !person.getFriends().contains(friendOfFriend)) {
                    potentialFriends.put(friendOfFriend, potentialFriends.getOrDefault(friendOfFriend, 0) + 3);
                }
            }
        }

        // 2. Same work/study place (weight = 2)
        for (Person p : people.values()) {
            if (!person.getFriends().contains(p) && p != person && p.getWorkStudyPlace().equals(person.getWorkStudyPlace())) {
                potentialFriends.put(p, potentialFriends.getOrDefault(p, 0) + 2);
            }
        }

        // 3. Same hometown (weight = 1)
        for (Person p : people.values()) {
            if (!person.getFriends().contains(p) && p != person && p.getHometown().equals(person.getHometown())) {
                potentialFriends.put(p, potentialFriends.getOrDefault(p, 0) + 1);
            }
        }

        // Sort by weight 
        List<Map.Entry<Person, Integer>> sortedList = new ArrayList<>(potentialFriends.entrySet());
        sortedList.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Person> result = new ArrayList<>();
        for (Map.Entry<Person, Integer> entry : sortedList) {
            result.add(entry.getKey());
        }
        return result;
    }

    // Display all people with the same hometown
    public List<Person> getSameHometown(Person person) {
        List<Person> result = new ArrayList<>();
        for (Person p : people.values()) {
            if (p.getHometown().equals(person.getHometown())) {
                result.add(p);
            }
        }
        return result;
    }

    // Display all people with the same work/study place
    public List<Person> getSameWorkStudyPlace(Person person) {
        List<Person> result = new ArrayList<>();
        for (Person p : people.values()) {
            if (p.getWorkStudyPlace().equals(person.getWorkStudyPlace())) {
                result.add(p);
            }
        }
        return result;
    }

    // Display a person and their friends
    public void displayPersonAndFriends(Person person) {
        System.out.println(person.getName() + " has the following friends:");
        for (Person friend : person.getFriends()) {
            System.out.println("- " + friend.getName());
        }
    }
    public List<Person> getMutualFriends(Person person1, Person person2) {
        Set<Person> mutualFriends = new HashSet<>(person1.getFriends());
        mutualFriends.retainAll(person2.getFriends());  
        return new ArrayList<>(mutualFriends);
    }
}

