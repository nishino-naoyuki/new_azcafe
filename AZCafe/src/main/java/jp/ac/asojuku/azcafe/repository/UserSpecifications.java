package jp.ac.asojuku.azcafe.repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import jp.ac.asojuku.azcafe.entity.UserTblEntity;
import jp.ac.asojuku.azcafe.form.UserSearchConditionForm;

public class UserSpecifications {
	private final static Integer NO_SELECT = UserSearchConditionForm.NO_SELECT;

	/**
     * 指定文字をメールアドレスに含むユーザーを検索する。
     */
    public static Specification<UserTblEntity> mailContains(String mail) {
        return StringUtils.isEmpty(mail) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("mail"),  "%" + mail + "%" );
			}
        };
    }

	/**
     * 指定文字を名前に含むユーザーを検索する。
     */
    public static Specification<UserTblEntity> nameContains(String name) {
        return StringUtils.isEmpty(name) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("name"),  "%" + name + "%" );
			}
        };
    }
    /**
     * 学科コードの検索
     * 
     * @param courseId
     * @return
     */
    public static Specification<UserTblEntity> homeroomEquals(Integer homeroomId) {
        return homeroomId == NO_SELECT || homeroomId == null ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("homeroomTbl").get("homeroomId"),  homeroomId );
			}
        };
    }

    /**
     * 役割コードの検索
     * 
     * @param courseId
     * @return
     */
    public static Specification<UserTblEntity> roleEquals(Integer roleId) {
        return roleId == NO_SELECT ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("role"),  roleId );
			}
        };
    }
    /**
     * 称号コードの検索
     * 
     * @param courseId
     * @return
     */
    public static Specification<UserTblEntity> levelEquals(Integer level) {
        return level == null || level == NO_SELECT ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.equal(root.get("userLevelSet").get("levelTbl").get("levelId"),  level );
			}
        };
    }
    /**
     * ニックネームの検索
     * @param nickName
     * @return
     */
    public static Specification<UserTblEntity> nicknameContains(String nickName) {
        return StringUtils.isEmpty(nickName) ? null : new Specification<UserTblEntity>() {
			@Override
			public Predicate toPredicate(Root<UserTblEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO 自動生成されたメソッド・スタブ
				return cb.like(root.get("nickName"),  "%"+nickName+"%" );
			}
        };
    }
}
