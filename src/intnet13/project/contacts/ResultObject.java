package intnet13.project.contacts;

import java.io.Serializable;

	public class ResultObject implements Serializable {
		private String column;
		private Object value;
		
		public ResultObject (String column, Object value) {
			this.column = column;
			this.value = value;
		}
		public String getColumn() {
			return column;
		}
		public Object getValue() {
			return value;
		}
	}