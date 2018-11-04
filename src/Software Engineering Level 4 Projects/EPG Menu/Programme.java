import java.util.Date;

public class Programme {

	private String id;
	private Date start; // if channel getstart is useless, remove the static
						// part
	private String desc;
	private String title;
	private Date end;
	// Initialise Loaders model

	public Programme() {
	}

	public Programme(String id, Date start, String desc, String title, Date end)

	{
		// TODO Auto-generated method stub
		this.id = id;
		this.start = start;
		this.desc = desc;
		this.title = title;
		this.end = end;

	}

	// getters

	public void setid(String id) {
		this.id = id;
	}

	public String getid() {
		return id;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getStart() {
		return start;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDesc() {
		return desc;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getEnd() {
		return end;
	}

	public String toString() {
		return this.title;
	}
}
