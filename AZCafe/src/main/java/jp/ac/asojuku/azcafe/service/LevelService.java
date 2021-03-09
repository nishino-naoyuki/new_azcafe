package jp.ac.asojuku.azcafe.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.ac.asojuku.azcafe.dto.LevelDto;
import jp.ac.asojuku.azcafe.entity.LevelTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillMapTblEntity;
import jp.ac.asojuku.azcafe.entity.SkillTblEntity;
import jp.ac.asojuku.azcafe.entity.UserLevelTblEntity;
import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.repository.AnswerRepository;
import jp.ac.asojuku.azcafe.repository.CommentRepository;
import jp.ac.asojuku.azcafe.repository.LevelRepository;
import jp.ac.asojuku.azcafe.repository.SkillMapRepository;
import jp.ac.asojuku.azcafe.repository.SkillRepository;
import jp.ac.asojuku.azcafe.repository.UserLevelRepository;
import jp.ac.asojuku.azcafe.repository.UserRepository;

@Service
public class LevelService {
	@Autowired
	LevelRepository levelRepository;
	@Autowired 
	UserRepository userRepository;
	@Autowired
	AnswerRepository answerRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	UserLevelRepository userLevelRepository;
	@Autowired
	SkillMapRepository skillMapRepository;
	@Autowired
	SkillRepository skillRepository;
	
	/**
	 * 称号の一覧を取得する
	 * @return
	 */
	public List<LevelDto> getList(){
		List<LevelDto> list = new ArrayList<>();
		List<LevelTblEntity> entityList =  levelRepository.findAll(Sort.by(Sort.Direction.ASC, "levelId"));
		
		for(LevelTblEntity entity : entityList) {
			LevelDto dto = new LevelDto();
			
			dto.setLevelId(entity.getLevelId());
			dto.setName(entity.getName());
			dto.setDescription(entity.getDescription());
			
			list.add(dto);
		}
		
		return list;
	}
	
	/**
	 * 称号
	 * @param userId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateLevel(Integer userId) {
		List<LevelTblEntity> entityList =  levelRepository.findAll(Sort.by(Sort.Direction.ASC, "levelId"));
		UserTblEntity userEntity = userRepository.getOne(userId);
		//称号の基準となる値を取得
		int point = userEntity.getPoint();
		int follower = userEntity.getFollowerNum();
		int good = userEntity.getGoodNum();
		int ansnum = answerRepository.getCount(userId);
		int commentNum = commentRepository.getCount(userId);
		
		List<UserLevelTblEntity> userLvList = new ArrayList<>();
		//称号の作成しなおし
		userLevelRepository.delete(userId);

		//スキルポイントの最高ポイントを取得
		List<SkillTblEntity> skillList = skillRepository.findAll();
		int maxPoint = -1;
		for(SkillTblEntity skill : skillList ) {
			SkillMapTblEntity skillmap = skillMapRepository.getOne(skill.getSkillId(), userId);
			int skillPt = 0;
			if( skillmap != null ) {
				skillPt = skillmap.getPoint();
			}
			if( skillPt > maxPoint ) {
				maxPoint = skillPt;
			}
		}
		
		for( LevelTblEntity entity : entityList ) {
			//称号チェック
			if( (entity.getPoint() <= point ) &&
				 (entity.getAnswer() <= ansnum) &&
				 (entity.getComment() <= commentNum ) &&
				 (entity.getFollower() <= follower ) &&
				 (entity.getGood() <= good) ) {
				//称号OK
				UserLevelTblEntity ule = new UserLevelTblEntity();
				ule.setLevelId(entity.getLevelId());
				ule.setUserId(userEntity.getUserId());
				userLvList.add(ule);				
			}
		}
				
		//更新
		userLevelRepository.saveAll(userLvList);
	}
}
