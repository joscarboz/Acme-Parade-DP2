
package converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Float;

@Component
@Transactional
public class FloatToStringConverter implements Converter<Float, String> {

	@Override
	public String convert(final Float ffloat) {
		String result;

		if (ffloat == null)
			result = null;
		else
			result = String.valueOf(ffloat.getId());

		return result;
	}

}
