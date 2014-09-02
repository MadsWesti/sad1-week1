import java.util.*;
public class GSStableMatching {
	private static boolean debug = false;
	static public void main(String[] args) {
		int N=0;
		int personCount = 0;
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
						Person p = (id%2 == 0) ? women.get((id-1)/2)  : men.get((id-1)/2); 
						String[] priorities = sc.nextLine().trim().split(" ");
						for (String priority : priorities) {
							int priorityId = Integer.parseInt(priority);
							p.addPriority((priorityId-1)/2);
						}
					}
					else{
						if (personCount < 2*N){ 
							int id = Integer.parseInt(token);

							if (id%2 == 0) {
								Woman w = new Woman((id-1)/2, sc.next(),N);
								women.add(w);
								if (debug == true) System.out.println(w.id +" "+ w.name);
							}
							else{
								Man m = new Man((id-1)/2, sc.next());
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
				if(currentWoman.prioritiesById[currentMan.id] < currentWoman.prioritiesById[husband.id]){
					if(debug) System.out.println(currentWoman.name + " switches partner to "+currentMan.name);
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
			priorities.add(id);
		}
		public int getNextPrority(){
			return priorities.poll();
		}
	}
	private static class Woman extends Person{
		public int[] prioritiesById;
		private int priorityCount = 0;
		public Woman(int id,  String name, int N){
			super(id, name);
			prioritiesById = new int[N];
		}
		public void addPriority(int id) {
			prioritiesById[id] = priorityCount++;
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
