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
