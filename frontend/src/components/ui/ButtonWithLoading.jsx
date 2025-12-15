import React from 'react';
import { Button } from './button';

export default function ButtonWithLoading({
  isLoading = false,
  onClick,
  children,
  loadingText = 'Загрузка...',
  ...rest
}) {
  return (
    <Button
      onClick={onClick}
      disabled={isLoading}
      {...rest}
    >
      {isLoading ? loadingText : children}
    </Button>
  );
}