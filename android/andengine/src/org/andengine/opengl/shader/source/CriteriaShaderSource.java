package org.andengine.opengl.shader.source;

import org.andengine.opengl.shader.exception.ShaderProgramException;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.util.criteria.IGLCriteria;

/**
 * (c) Zynga 2011
 *
 * @author Nicolas Gramlich <ngramlich@zynga.com>
 * @since 16:30:12 - 10.10.2011
 */
public class CriteriaShaderSource implements IShaderSource {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final CriteriaShaderSourceEntry[] mCriteriaShaderSourceEntries;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CriteriaShaderSource(final CriteriaShaderSourceEntry ... pCriteriaShaderSourceEntries) {
		this.mCriteriaShaderSourceEntries = pCriteriaShaderSourceEntries;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public String getShaderSource(final GLState pGLState) {
        for (final CriteriaShaderSourceEntry criteriaShaderSourceEntry : this.mCriteriaShaderSourceEntries) {
            if (criteriaShaderSourceEntry.isMet(pGLState)) {
                return criteriaShaderSourceEntry.getShaderSource();
            }
        }
		throw new ShaderProgramException("No " + CriteriaShaderSourceEntry.class.getSimpleName() + " met!");
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	static class CriteriaShaderSourceEntry {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		private final String mShaderSource;
		private final IGLCriteria[] mGLCriterias;

		// ===========================================================
		// Constructors
		// ===========================================================
		
		CriteriaShaderSourceEntry(final String pShaderSource) {
			this(pShaderSource, (IGLCriteria[]) null);
		}

		CriteriaShaderSourceEntry(final String pShaderSource, final IGLCriteria... pCriterias) {
			this.mGLCriterias = pCriterias;
			this.mShaderSource = pShaderSource;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		String getShaderSource() {
			return this.mShaderSource;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		boolean isMet(final GLState pGLState) {
			if(this.mGLCriterias != null) {
				for(IGLCriteria gLCriteria : this.mGLCriterias) {
					if(!gLCriteria.isMet(pGLState)) {
						return false;
					}
				}
			}
			return true;
		}

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
