
/* Exemple de info_hash :
 * Anno : 1dd2a90fd458c1e995e842cb0f429e368b58bc16
 *
 */

/*Exemple de peer_id :
 * -EA0001-AZ2060-
 */


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TrackerCommunicator tracker = new TrackerCommunicator(
				"http://tracker.trackerfix.com/announce",
				"1dd2a90fd458c1e995e842cb0f429e368b58bc16",
				"-EA0001-AZ2060-",
				"192.168.0.254",
				"6881",
				"0",
				"0",
				"started");
		String info = tracker.HTTPGet();
		System.out.println(info);
	}

}
