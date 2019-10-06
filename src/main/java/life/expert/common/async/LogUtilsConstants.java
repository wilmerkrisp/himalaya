package life.expert.common.async;
//@Header@
//--------------------------------------------------------------------------------
//
//                          himalaya  life.expert.common.async
//                           wilmer 2019/09/26
//
//--------------------------------------------------------------------------------

public interface LogUtilsConstants
	{
	static final long DEFAULT_DELAY_ = 1;
	
	static final String FORMAT_ = "{}   {}";
	
	static final String FORMAT_DELAY_ = "{}   {} delay({})";
	
	static final String FORMAT_IN_ = "{}   {} in({})";
	
	static final String FORMAT_IN2_ = "{}   {} in({}) in({})";
	
	static final String FORMAT_IN_DELAY_ = "{}   {} in({}) delay({})";
	
	static final String FORMAT_IN2_DELAY_ = "{}   {} in({}) in({}) delay({})";
	
	static final String FORMAT_OUT_ = "{}   {} out({})";
	
	static final String FORMAT_OUT_DELAY_ = "{}   {} out({}) delay({})";
	
	static final String FORMAT_INOUT_ = "{}   {} in({}) out({})";
	
	static final String FORMAT_IN2OUT_ = "{}   {} in({}) in({}) out({})";
	
	static final String FORMAT_INOUT_DELAY_ = "{}   {} in({}) out({}) delay({})";
	
	static final String FORMAT_IN2OUT_DELAY_ = "{}   {} in({}) in({}) out({}) delay({})";
	}
