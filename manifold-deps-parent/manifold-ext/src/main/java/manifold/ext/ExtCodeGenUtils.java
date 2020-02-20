package manifold.ext;

import manifold.api.gen.AbstractSrcMethod;
import manifold.api.gen.SrcClass;
import manifold.api.gen.SrcParameter;
import manifold.api.gen.SrcType;
import manifold.ext.api.This;

import java.util.List;
import java.util.Objects;

public class ExtCodeGenUtils {

	private static final SrcType objectSrcType = new SrcType(Object.class);

	private static boolean isTypeVariable(SrcType type) {
		boolean result;
		if (type.hasExtends() || type.hasSuper()) {
			result = true;
		} else {
			String fqName = type.getFqName();
			if (fqName.indexOf('.') == -1) {
				try {
					Class.forName(fqName);
					result = false;
				} catch (ClassNotFoundException ex) {
					result = true;
				}
			} else {
				result = false;
			}
		}
		return result;
	}

	private static SrcType resolveLowerBound(AbstractSrcMethod<?> method, SrcType type) {
		SrcType result;
		{
			SrcType effectiveType = method.getTypeVariables().stream()
					.filter(typeVariable -> Objects.equals(typeVariable.getName(), type.getName()))
					.findFirst().orElse(type);
			if (isTypeVariable(type)) {
				if (effectiveType.hasExtends()) {
					// TODO: Cases like <T extends List<T> & Collection<T>> will result in incorrect type
					//  (Object instead of Collection<?>), but they are effectively senseless so just think
					//  what you write until this issue is fixed.
					if (effectiveType.getBounds().size() == 1) {
						result = effectiveType.getBounds().get(0);
					} else {
						result = objectSrcType;
					}
				} else {
					result = objectSrcType;
				}
			} else {
				result = type;
			}
		}
		return result;
	}

	public static boolean hasValidThisAnnotation(AbstractSrcMethod<?> method, SrcClass extendedType) {
		boolean result;
		List<SrcParameter> params = method.getParameters();
		if (params.size() > 0) {
			SrcParameter firstParameter = params.get(0);
			if (firstParameter.hasAnnotation(This.class)) {
				SrcType effectiveFirstParameterType = resolveLowerBound(method, firstParameter.getType());
				result = effectiveFirstParameterType.getName().endsWith(extendedType.getSimpleName());
			} else {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}
}
