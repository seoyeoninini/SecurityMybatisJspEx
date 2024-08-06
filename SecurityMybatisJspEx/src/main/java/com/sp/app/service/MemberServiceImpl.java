package com.sp.app.service;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sp.app.domain.Member;
import com.sp.app.mail.Mail;
import com.sp.app.mail.MailSender;
import com.sp.app.mapper.MemberMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
	private final MemberMapper mapper;
	private final MailSender mailSender;
	private final PasswordEncoder bcryptEncoder;
	
	@Override
	public void insertMember(Member dto) throws Exception {
		try {
			if (dto.getEmail1().length() != 0 && dto.getEmail2().length() != 0) {
				dto.setEmail(dto.getEmail1() + "@" + dto.getEmail2());
			}

			if (dto.getTel1().length() != 0 && dto.getTel2().length() != 0 && dto.getTel3().length() != 0) {
				dto.setTel(dto.getTel1() + "-" + dto.getTel2() + "-" + dto.getTel3());
			}

			// 패스워드 암호화
			String encPassword = bcryptEncoder.encode(dto.getUserPwd());
			dto.setUserPwd(encPassword);
			
			long memberSeq = mapper.memberSeq();
			dto.setMemberIdx(memberSeq);

			// 회원정보 저장
			mapper.insertMember(memberSeq);

			mapper.insertMember12(dto); // member1, member2 테이블 동시에
			
			// --------------------------------------
			// 권한 저장
			dto.setAuthority("USER");
			mapper.insertAuthority(dto);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateMembership(Map<String, Object> map) throws Exception {
		try {
			mapper.updateMembership(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateLastLogin(String userId) throws Exception {
		try {
			mapper.updateLastLogin(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateMember(Member dto) throws Exception {
		try {
			if (dto.getEmail1().length() != 0 && dto.getEmail2().length() != 0) {
				dto.setEmail(dto.getEmail1() + "@" + dto.getEmail2());
			}

			if (dto.getTel1().length() != 0 && dto.getTel2().length() != 0 && dto.getTel3().length() != 0) {
				dto.setTel(dto.getTel1() + "-" + dto.getTel2() + "-" + dto.getTel3());
			}

			// --------------------------------------
			boolean bPwdUpdate = ! isPasswordCheck(dto.getUserId(), dto.getUserPwd());
			if( bPwdUpdate ) {
				// 패스워드가 변경된 경우만 member1 테이블의 패스워드 변경
				String encPassword = bcryptEncoder.encode(dto.getUserPwd());
				// System.out.println(encPassword);
				dto.setUserPwd(encPassword);
				
				mapper.updateMember1(dto);
			}			
			
			mapper.updateMember2(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}

	@Override
	public Member findById(String userId) {
		Member dto = null;

		try {
			dto = mapper.findById(userId);

			if (dto != null) {
				if (dto.getEmail() != null) {
					String[] s = dto.getEmail().split("@");
					dto.setEmail1(s[0]);
					dto.setEmail2(s[1]);
				}

				if (dto.getTel() != null) {
					String[] s = dto.getTel().split("-");
					dto.setTel1(s[0]);
					dto.setTel2(s[1]);
					dto.setTel3(s[2]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public Member findById(long memberIdx) {
		Member dto = null;

		try {
			dto = mapper.findByMemberIdx(memberIdx);

			if (dto != null) {
				if (dto.getEmail() != null) {
					String[] s = dto.getEmail().split("@");
					dto.setEmail1(s[0]);
					dto.setEmail2(s[1]);
				}

				if (dto.getTel() != null) {
					String[] s = dto.getTel().split("-");
					dto.setTel1(s[0]);
					dto.setTel2(s[1]);
					dto.setTel3(s[2]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dto;
	}

	@Override
	public void deleteMember(Map<String, Object> map) throws Exception {
		try {
			map.put("membership", 0);
			updateMembership(map);

			mapper.deleteMember2(map);
			mapper.deleteMember1(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void generatePwd(Member dto) throws Exception {
		// 10 자리 임시 패스워드 생성
		StringBuilder sb = new StringBuilder();
		Random rd = new Random();
		String s = "!@#$%^&*~-+ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		for (int i = 0; i < 10; i++) {
			int n = rd.nextInt(s.length());
			sb.append(s.substring(n, n + 1));
		}

		String result;
		result = dto.getUserId() + "님의 새로 발급된 임시 패스워드는 <b>"
				+ sb.toString()
				+ "</b> 입니다.<br>"
				+ "로그인 후 반드시 패스워드를 변경 하시기 바랍니다.";

		Mail mail = new Mail();
		mail.setReceiverEmail(dto.getEmail());

		mail.setSenderEmail("보내는사람이메일@도메인");
		mail.setSenderName("관리자");
		mail.setSubject("임시 패스워드 발급");
		mail.setContent(result);
		
		// 테이블의 패스워드 변경
		dto.setUserPwd(sb.toString());
		updatePwd(dto);

		// 메일전숭
		boolean b = mailSender.mailSend(mail);

		if (! b) {
			throw new Exception("이메일 전송중 오류가 발생했습니다.");
		}
		
	}

	@Override
	public boolean isPasswordCheck(String userId, String userPwd) {
		// --------------------------------------
		Member dto = findById(userId);
		if(dto == null) {
			return false;
		}
		
		// 패스워드 비교(userPwd를 암호화 해서 dto.getUserPwd()와 비교하면 안된다.)
		//                 userPwd를 암호화하면 가입할때의 암호화 값과 다름. 암호화할때 마다 다른 값
		
		return bcryptEncoder.matches(userPwd, dto.getUserPwd());
		// --------------------------------------
	}

	@Override
	public void updatePwd(Member dto) throws Exception {
		// --------------------------------------
		if( isPasswordCheck(dto.getUserId(), dto.getUserPwd()) ) {
			throw new RuntimeException("패스워드가 기존 패스워드와 일치합니다.");
		}

		try {
			String encPassword = bcryptEncoder.encode(dto.getUserPwd());
			dto.setUserPwd(encPassword);
			
			mapper.updateMember1(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		// --------------------------------------
	}

	@Override
	public int checkFailureCount(String userId) {
		int result = 0;
		try {
			result = mapper.checkFailureCount(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public void updateFailureCount(String userId) throws Exception {
		try {
			mapper.updateFailureCount(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateFailureCountReset(String userId) throws Exception {
		try {
			mapper.updateFailureCountReset(userId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public void updateMemberEnabled(Map<String, Object> map) throws Exception {
		try {
			mapper.updateMemberEnabled(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}		
	}

	@Override
	public void insertMemberState(Member dto) throws Exception {
		try {
			mapper.insertMemberState(dto);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	@Override
	public List<String> listAuthority(String userId) {
		List<String> list = null;
		
		try {
			list = mapper.listAuthority(userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}	
}
