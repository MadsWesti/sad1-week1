import java.util.*;
public class GSStableMatching {
	static public void main(String[] args) {
		int N=0;
		int personCount = 0;
		boolean debug = false;
		ArrayList<Woman> women = new ArrayList<Woman>();
		LinkedList<Man> men = new LinkedList<Man>();
		
		Scanner sc = new Scanner(System.in);
			while (sc.hasNext()){
				String token = sc.next();
				if (token.equals("#") || token.startsWith("#")){
					String line = sc.nextLine();
					if (debug == true) System.out.println("Skipped: "+line);
				}
				else {
					if (token.startsWith("n=")){
						N = Integer.parseInt(token.substring(2));
						if (debug == true) System.out.println("N=" + N);
					}
					else if(token.endsWith(":")) {
						int id = Integer.parseInt(token.substring(0, token.length()-1));
						Person p = (id%2 == 0) ? women.get(id/2-1)  : men.get((id+1)/2-1);
						String priorities = sc.nextLine(); 
						for (int i = 0; i < priorities.length(); i++) {
							char c = priorities.charAt(i);
							if (c != ' ') {
								int partnerId = Character.getNumericValue(c);
								p.addPriority(partnerId);
							}
						}
					}
					else{
						if (personCount < 2*N){ 
							int id = Integer.parseInt(token);
							if (id%2 == 0) {
								Woman w = new Woman(id/2-1, sc.next());
								women.add(w);
								if (debug == true) System.out.println(w.id +" "+ w.name);
							}
							else{
								Man m = new Man((id+1)/2-1, sc.next());
								men.add(m);
								if (debug == true) System.out.println(m.id +" "+ m.name);
							}
							personCount++;
						}
					}
				}
				
			}
		sc.close();
		
		Man[] marriedMen = matching(men, women);
		for (Man m : marriedMen){
			System.out.println(m.name + " -- " + m.marriedTo.name);
		}
	}
	
	private static Man[] matching(LinkedList<Man> men, ArrayList<Woman> women) {
		boolean debug = true;
		Man[] marriedMen = new Man[men.size()];
		while(!men.isEmpty()){
			Man currentMan = men.pop();
			Woman currentWoman = women.get(currentMan.getNextPrority());
			if(debug) System.out.println(currentMan.name+" proposes to "+currentWoman.name);
			if (!currentWoman.isMarried()) {
				if(debug) System.out.println("Gets married");
				currentMan.marriedTo = currentWoman;
				currentWoman.marriedTo = currentMan;
				marriedMen[currentMan.id] = currentMan;
			}
			else{
				Man husband = (Man) currentWoman.marriedTo;
				if(currentWoman.prioritiesById.get(currentMan.id) < currentWoman.prioritiesById.get(husband.id)){
					if(debug) System.out.println(currentWoman.name+" switches partner");
					men.add(husband);
					currentMan.marriedTo = currentWoman;
					currentWoman.marriedTo = currentMan;
					marriedMen[currentMan.id] = currentMan;
				}
				else{
					if(debug) System.out.println("Gets rejected");
					men.add(currentMan);
				}
			}
		}
		return marriedMen;
	}
	
	private static class Man extends Person{
		public LinkedList<Integer> priorities = new LinkedList<Integer>();
		public Man(int id,  String name){
			super(id,name);
		}
		public void addPriority(int id) {
			priorities.add(id/2-1);
		}
		public int getNextPrority(){
			return priorities.poll();
		}
	}
	private static class Woman extends Person{
		public HashMap<Integer, Integer> prioritiesById = new HashMap<Integer, Integer>();
		public Woman(int id,  String name){
			super(id, name);
		}
		public void addPriority(int id) {
			int priority = prioritiesById.size() + 1;
			prioritiesById.put((id+1)/2-1, priority);
		}
	}
	private static class Person {
		public Person marriedTo = null;
		public int id;
		public String name;
		public Person(int id, String name){
			this.id = id;
			this.name = name;
		}
		public boolean isMarried(){
			if (marriedTo != null) return true;
			else return false;
		}
		public void addPriority(int id){}
	}
}
