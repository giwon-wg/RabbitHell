import styled from 'styled-components';

export const LoginContainer = styled.div`
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	height: 100vh;
	background-color: #f5f5f5;
`;

export const GameTitle = styled.img`
	width: 300px;
	margin-bottom: 40px;
`;

export const LoginForm = styled.form`
	display: flex;
	flex-direction: column;
	width: 300px;
	margin-bottom: 20px;
`;

export const Input = styled.input`
	height: 40px;
	margin-bottom: 12px;
	padding: 8px;
	font-size: 14px;
	border: 1px solid #ccc;
	border-radius: 4px;
`;

export const SubmitButton = styled.button`
	height: 45px;
	background-color: #4caf50;
	border: none;
	border-radius: 6px;
	color: white;
	font-weight: bold;
	cursor: pointer;

	&:hover {
		background-color: #45a049;
	}
`;

export const KakaoButton = styled.button`
	width: 300px;
	height: 50px;
	background-color: #FEE500;
	border: none;
	border-radius: 8px;
	cursor: pointer;
	font-size: 16px;
	font-weight: bold;
	color: #000000;

	&:hover {
		background-color: #E6CF00;
	}
`;


// import styled from 'styled-components';
//
// export const LoginContainer = styled.div`
//   display: flex;
//   flex-direction: column;
//   align-items: center;
//   justify-content: center;
//   height: 100vh;
//   background-color: #f5f5f5;
// `;
//
// export const GameTitle = styled.img`
//   width: 300px;
//   margin-bottom: 40px;
// `;
//
// export const KakaoButton = styled.button`
//   width: 300px;
//   height: 50px;
//   background-color: #FEE500;
//   border: none;
//   border-radius: 8px;
//   cursor: pointer;
//   font-size: 16px;
//   font-weight: bold;
//   color: #000000;
//
//   &:hover {
//     background-color: #E6CF00;
//   }
// `;
