package club.hoy.weixin.endpoint;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Status {
	
	public final static ResponseEntity<String> _503 = new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE);
	public final static ResponseEntity<String> _200 = new ResponseEntity<String>(HttpStatus.OK);
	
}
