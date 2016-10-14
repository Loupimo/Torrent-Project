
/* Exemple de info_hash :
 * Anno : 1dd2a90fd458c1e995e842cb0f429e368b58bc16
 * ACBF : 4DB1286DCC5C979F9A3A631DC37E17B28C60CBD6
 *
 */

/*Exemple de peer_id :
 * -EA0001-AZ2060-
 */


public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TrackerCommunicator tracker = new TrackerCommunicator(
				"http://t411.download/7d8c1038e6ddb08272ccdb7e1e13d6c0/announce",
				"4DB1286DCC5C979F9A3A631DC37E17B28C60CBD6",
				"-AZ2200-6wfG2wk6wWLc",
				/*"192.168.0.254",*/
				"6883",
				0,
				0,
				0,
				"started");
		String info = tracker.HTTPGet();
		System.out.println(info);
	}

}
