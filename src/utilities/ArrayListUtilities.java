package utilities;

import java.util.ArrayList;

public class ArrayListUtilities<T> {

	public static <T extends Object> void removeObjectFromArrayList(ArrayList<T> arrayListOfObjects, T object) {
		for (int i = 0; i < arrayListOfObjects.size(); i++) {
			if (arrayListOfObjects.get(i) == object) {
				arrayListOfObjects.remove(i);
			}
		}
	}
}
