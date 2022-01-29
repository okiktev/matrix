package com.delfin.matrix;

import java.awt.Component;

public interface Matrix {

	enum Type {
		VOLUNTARY("voluntary"), $1999("1999"), $2021("2021");

		private String type;

		Type(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			return type;
		}
	};

	void draw(Component canvas);

	void destroy();

}
